package com.provider.unobridge.base

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.provider.unobridge.databinding.ToolbarLayoutBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext


abstract class ScopedFragment : Fragment(), CoroutineScope {
    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    abstract fun stopBlur()


    /* fun stopBlur(root: ViewGroup) {
         Blurry.delete(root)
     }

     fun startBlur(root: ViewGroup) {
         Blurry.with(requireContext()).radius(10).sampling(2).onto(root)
     }*/

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    fun hideKeyboard(v: View) {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }


    fun showToast(message: String) {
        val toast = Toast.makeText(requireContext(), message, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }


    fun initToolbar(binding: ToolbarLayoutBinding, title: String, subTitle: String) {
        binding.tvTitle.text = title
        binding.tvSubTitle.text = subTitle
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    override fun onResume() {
        super.onResume()
        hideProgress()


    }

    override fun onPause() {
        super.onPause()
        hideProgress()

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("ScopedFragmentOnCreatee", "OnCreateeee")
        return super.onCreateView(inflater, container, savedInstanceState)

    }

    fun showProgress() {
        MyProgress.show(requireActivity() as AppCompatActivity)
    }

    fun hideProgress() {
        MyProgress.hide(requireActivity() as AppCompatActivity)

    }


    fun setFullScreen() {


    }

    fun setLightStatusBarColor() {
        (requireActivity() as ScopedActivity).apply {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }
    }


}