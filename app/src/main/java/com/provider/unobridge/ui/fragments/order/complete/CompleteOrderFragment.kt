package com.provider.unobridge.ui.fragments.order.complete

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.provider.unobridge.R
import com.provider.unobridge.base.Utils.Companion.disableMultiTap
import com.provider.unobridge.databinding.FragmentCompleteOrderBinding
import java.io.File


class CompleteOrderFragment : Fragment(),ClickListener {

    private lateinit var mBinding: FragmentCompleteOrderBinding
    private fun fileFromAsset(name: String): File =
        File("${requireActivity().cacheDir}/$name").apply {
            writeBytes(
                requireActivity().assets.open(
                    name
                ).readBytes()
            )
        }

    private val storagePermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            with(mBinding.root) {
                when {
                    granted -> savePdfToCache()
                    shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ->
                        showPermissionError()
                    else -> showPermissionError()
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_complete_order, container, false)
      mBinding.clickHandler = this
        return mBinding.root
    }

    private fun sendToWhatsApp(uri: Uri) {
        startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            setPackage("com.whatsapp")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            putExtra(Intent.EXTRA_SUBJECT, "Share your experience")
            putExtra(Intent.EXTRA_TEXT, "${getString(R.string.raju_electrician_website)}/feedback")
            putExtra(Intent.EXTRA_TEXT, "upi://pay?pa=raju@okhdfcbank&pn=PayeeName&tn=PaymentMessage&cu=INR';")
            putExtra(Intent.EXTRA_STREAM, uri)
        }, "Share your experience"))
    }

    private fun showPermissionError() {
        Toast.makeText(
            requireContext(),
            "Please grant Storage permission for sharing.",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun savePdfToCache() {
        val file = fileFromAsset("mahesh_gandhi.pdf")
        sendToWhatsApp(getUriFromFile(file))
    }

    private fun getUriFromFile(file: File): Uri {
        val apkURI: Uri = FileProvider.getUriForFile(
            requireContext(), requireActivity()
                .packageName.toString() + ".provider", file
        )
        return apkURI
    }

    override fun onPaymentLink(view: View) {
        view.disableMultiTap()

    }

    override fun onReceiveFeedback(view: View) {
        view.disableMultiTap()
        findNavController().navigate(R.id.finishedOrderFragment)
    }

    override fun onInvoice(view: View) {
        view.disableMultiTap()
    }

    override fun onShareViaWhatsApp(view: View) {
        view.disableMultiTap()
        storagePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
}