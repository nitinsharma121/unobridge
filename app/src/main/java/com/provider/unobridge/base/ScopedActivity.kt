package com.provider.unobridge.base

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class ScopedActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var job: Job


    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()

    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }


    fun hideKeyboard(v: View) {
        val imm =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

    fun disable(view: View) {
        view.isEnabled = false
        android.os.Handler().postDelayed({

            view.isEnabled = true
        }, 1000)
    }

    fun showToast(message: String, type: String) {
        if (type.equals("success")) {
            val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        } else {
            val transaction = supportFragmentManager.beginTransaction()
//            ErrorMessageFragment(message).show(transaction, "SOME_TAG")
        }


    }


    fun setStatusBarTransparent() {
        window.apply {
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }
    }

    fun setStatusBarColor(color: Int) {
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources!!.getColor(color)
    }


    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {
        super.onPause()
        hideKeyboardByActivity(this)
    }

    fun hideKeyboardByActivity(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showProgress() {
        MyProgress.show(this)
    }

    fun hideProgress() {
        MyProgress.hide(this)

    }


}