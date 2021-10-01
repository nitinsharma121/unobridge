package com.provider.unobridge.base

import android.util.Log
import androidx.annotation.NonNull
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager


abstract class EndlessRecyclerViewScrollListener : RecyclerView.OnScrollListener {
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private var visibleThreshold = 10

    // The current offset index of data you have loaded
    private var currentPage = 1

    // The total number of items in the dataset after the last load
    private var previousTotalItemCount = 0

    // True if we are still waiting for the last set of data to load.
    private var loading = true

    // Sets the starting page index
    private val startingPageIndex = 1

    private var mLayoutManager: RecyclerView.LayoutManager

    constructor(layoutManager: LinearLayoutManager) {
        this.mLayoutManager = layoutManager

    }

    constructor(layoutManager: GridLayoutManager) {
        this.mLayoutManager = layoutManager
        visibleThreshold *= layoutManager.spanCount
    }

    constructor(layoutManager: StaggeredGridLayoutManager) {
        this.mLayoutManager = layoutManager
        visibleThreshold *= layoutManager.spanCount
    }

    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }

    // This happens many times a second during a scroll, so be wary of the code you place here.
    // We are given a few useful parameters to help us work out if we need to load some more data,
    // but first we check if we are waiting for the previous load to finish.
    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(view, dx, dy);

        if (dy < 1) {
            return
        }
        var lastVisibleItemPosition = 0
        var firstVisibleItemPosition = 0
        val totalItemCount = mLayoutManager.itemCount

        when (mLayoutManager) {
            is StaggeredGridLayoutManager -> {
                val lastVisibleItemPositions =
                    (mLayoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(null)

                // get maximum element within the list
                lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions)
            }
            is GridLayoutManager -> {
                lastVisibleItemPosition =
                    (mLayoutManager as GridLayoutManager).findLastVisibleItemPosition()
                firstVisibleItemPosition =
                    (mLayoutManager as GridLayoutManager).findFirstVisibleItemPosition()
            }
            is LinearLayoutManager -> {

                Log.e(
                    "LastVisibleItem",
                    "Posfdg${(mLayoutManager as LinearLayoutManager).findFirstVisibleItemPosition()}"
                )
                lastVisibleItemPosition =
                    (mLayoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                firstVisibleItemPosition =
                    (mLayoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            }
        }

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        // threshold should reflect how many total columns there are too

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state

        Log.e("previousItemTotalcount", previousTotalItemCount.toString())
        Log.e("totlaItem", totalItemCount.toString())
        if (totalItemCount <= previousTotalItemCount) {
            this.currentPage = this.startingPageIndex
            this.previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) {
                this.loading = true
            }
        }
        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && totalItemCount > previousTotalItemCount) {
            loading = false
            previousTotalItemCount = totalItemCount
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        // threshold should reflect how many total columns there are too
        if (!loading && lastVisibleItemPosition >= totalItemCount - 4 && firstVisibleItemPosition >= 0) {
            Log.e("Paginggg", "detected")
            currentPage++
            onLoadMore(currentPage, totalItemCount, view)
            loading = true
        }
    }


    // Call this method whenever performing new searches
    fun resetState() {
        this.currentPage = this.startingPageIndex
        this.previousTotalItemCount = 0
        this.loading = true
    }

    // Defines the process for actually loading more data based on page
    abstract fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?)
    abstract fun onScrollFirstVisibleItem(position: Int, visiblePosition: Int)

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            // supply a positive number to recyclerView.canScrollVertically(int direction) to check if scrolling down.
            val canScrollDownMore = recyclerView.canScrollVertically(1)
            // If recyclerView.canScrollVertically(1) returns false it means you're at the end of the list.
            if (!canScrollDownMore) {
                onScrolled(recyclerView, 0, 1)
            }
            getNewPosition(recyclerView)
        }
    }

    fun getNewPosition(@NonNull recyclerView: RecyclerView) {
        try {
            val layoutManager =
                recyclerView.layoutManager as LinearLayoutManager?
            if (layoutManager != null) {
                var recyclerVisiblePosition = layoutManager.findFirstCompletelyVisibleItemPosition()
                var position = 0
                Log.e("RecylerVisiblePositon", recyclerVisiblePosition.toString())
                if (recyclerVisiblePosition == -1) {

                    position = layoutManager.findLastCompletelyVisibleItemPosition()
                    Log.e("LastCompleteVisivle", position.toString())
                    if (position == -1) {
                        position = layoutManager.findFirstVisibleItemPosition()
                        Log.e("LastVisivle", position.toString())
                    }
                } else {
                    position = recyclerVisiblePosition
                }
                onScrollFirstVisibleItem(position, layoutManager.findLastVisibleItemPosition())
            }
        } catch (e: Exception) {
            e.printStackTrace()

        }

    }
}