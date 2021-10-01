package com.provider.unobridge.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.provider.unobridge.databinding.LayoutProgressDialogBinding

import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.Circle


class MyProgress : DialogFragment() {

    lateinit var mBinding: LayoutProgressDialogBinding

    companion object {

        var isShowing = false

        fun show(activity: AppCompatActivity) {
            if (isShowing) return
            val dialog = MyProgress()
//            dialog.setStyle(STYLE_NO_FRAME,R.style.CardDetailsDialogStyle)
            dialog.show(activity.supportFragmentManager, "_progress_dialog")
//            activity.supportFragmentManager.executePendingTransactions()
            isShowing = true
        }

        fun hide(activity: AppCompatActivity) {
            val pD = activity.supportFragmentManager.findFragmentByTag("_progress_dialog")
            try {
                if (pD != null) {
                    activity.supportFragmentManager.beginTransaction().remove(pD).commit()
//                activity.supportFragmentManager.executePendingTransactions()
                }
            } catch (e: Exception) {
                if (pD != null) {
                    activity.supportFragmentManager.beginTransaction().remove(pD).commitNow()
                }
                e.printStackTrace()


            }

            isShowing = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyProgressDialog);

        isCancelable = false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)


        val builder = AlertDialog.Builder(requireContext())
        val lI =
            requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mBinding = LayoutProgressDialogBinding.inflate(lI, null, false)
        builder.setView(mBinding.root)
        val aD = builder.create()
        val doubleBounce: Sprite = Circle()
        mBinding.spinKit.setIndeterminateDrawable(doubleBounce)
//        aD.window?.setDimAmount(0.0f)
//        aD.window?.setBackgroundDrawableResource(android.R.color.transparent)
        resizeDialog(aD)
        return aD
    }

    private fun resizeDialog(dialog: Dialog?) {
        if (dialog != null) {
            val displayMetrics = DisplayMetrics()
            dialog.window?.apply {
                windowManager.defaultDisplay.getMetrics(displayMetrics)
                val layoutParams = WindowManager.LayoutParams()
                layoutParams.copyFrom(attributes)
                val dialogWindowWidth = (displayMetrics.widthPixels * 0.5f).toInt()
                val dialogWindowHeight = (displayMetrics.heightPixels * 0.5f).toInt()
                layoutParams.width = dialogWindowWidth
                layoutParams.height = dialogWindowHeight
                attributes = layoutParams
                setBackgroundDrawableResource(android.R.color.transparent);
                clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        }
    }
}