package com.provider.unobridge.ui.fragments.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.provider.unobridge.R
import com.provider.unobridge.base.ScopedFragment
import com.provider.unobridge.data.ProfileParams
import com.provider.unobridge.databinding.UserDetailsFragmentBinding
import com.provider.unobridge.ui.activities.AuthHandlerActivity
import com.provider.unobridge.ui.fragments.auth.viewModel.LoginViewModel
import com.provider.unobridge.ui.fragments.auth.viewModel.LoginViewModelFactory
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import id.zelory.compressor.constraint.quality
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import java.io.File

class UserDetailsFragment : ScopedFragment(), KodeinAware {
    lateinit var mBinding: UserDetailsFragmentBinding
    override val kodein by lazy { (activity?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: LoginViewModelFactory by instance()
    lateinit var mViewModel: LoginViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!::mBinding.isInitialized) {
            mViewModel =
                ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
            mBinding = UserDetailsFragmentBinding.inflate(inflater, container, false).apply {
                clickHandler = ClickHandler()
            }
        }

        (requireActivity() as AuthHandlerActivity).listener = this.ClickHandler()

        initToolbar(
            mBinding.tToolbar,
            getString(R.string.enter_personal_details),
            getString(R.string.enter_otp_msg)
        )
        mBinding.tToolbar.tvSubTitle.text =
            Html.fromHtml("Let us know your " + "<font color=#0068FF>" + "personal details" + "</font>" + ".")
        mViewModel.profileParams =
            arguments?.get(getString(R.string.profile_params)) as ProfileParams

        mBinding.tToolbar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
        return mBinding.root
    }


    inner class ClickHandler : AuthHandlerActivity.onClickListeners {


        fun addProfilePic() {
            runWithPermissions(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE

            ) {
                ImagePicker.with(this@UserDetailsFragment)
                    .crop()
                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)            //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(
                        1080,
                        1080
                    )    //Final image resolution will be less than 1080 x 1080(Optional)
                    .start()
            }

        }


        override fun onClickNext() {

            if (mViewModel.profileParams.profileImage == null) {
                showToast(getString(R.string.profile_pic_error))
            } else if (mBinding.etName.text.toString().isEmpty()) {
                showToast(getString(R.string.name_empty_error))
            } else if (mBinding.etEmail.text.toString().isEmpty()) {
                showToast(getString(R.string.email_empty_error))
            } else if (!isValidEmailId(mBinding.etEmail.text.toString())) {
                showToast(getString(R.string.email_valid_error))
            } else {
                mViewModel.profileParams.email = mBinding.etEmail.text.toString()
                mViewModel.profileParams.name = mBinding.etName.text.toString()


                if (mViewModel.profileParams.userType == getString(R.string.driver)) {
                    findNavController().navigate(
                        R.id.vehicle_details_fragment, bundleOf(
                            getString(R.string.profile_params) to mViewModel.profileParams
                        )
                    )
                } else {
                    findNavController().navigate(
                        R.id.aadhar_details_fragment, bundleOf(
                            getString(R.string.profile_params) to mViewModel.profileParams
                        )
                    )
                }

            }
        }


    }


    override fun stopBlur() {

    }

    override fun onDestroy() {
        super.onDestroy()
        hideProgress()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val uri: Uri = data?.data!!

            var compressedImageFile: File? = null
            lifecycleScope.launch {
                val process = async {
                    compressedImageFile =
                        Compressor.compress(requireContext(), File(uri.path)) {
                            default(format = Bitmap.CompressFormat.WEBP)
                            quality(30)

                        }
                }
                process.await()

                Log.e("ComprredFileKB", (compressedImageFile?.length()!! / 1024).toString())

                var fileUri = Uri.fromFile(compressedImageFile!!)
                mBinding.ivImage.setImageURI(fileUri)

                mViewModel.profileParams.profileImage = MultipartBody.Part.createFormData(
                    "profileImage",
                    compressedImageFile?.name,
                    RequestBody.create(
                        "image/*".toMediaTypeOrNull(),
                        compressedImageFile!!

                    )
                )


            }
        }
    }

    private fun isValidEmailId(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}