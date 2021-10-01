package com.provider.unobridge.ui.fragments.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.provider.unobridge.R
import com.provider.unobridge.base.Prefs
import com.provider.unobridge.base.ScopedFragment
import com.provider.unobridge.data.ProfileParams
import com.provider.unobridge.data.responeClasses.UserAadharDetails
import com.provider.unobridge.databinding.AadharDetailsFragmentBinding
import com.provider.unobridge.ext.showImage
import com.provider.unobridge.ui.activities.AuthHandlerActivity
import com.provider.unobridge.ui.activities.MainActivity
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

class AaadharDetailsFragment : ScopedFragment(), KodeinAware {
    lateinit var mBinding: AadharDetailsFragmentBinding
    override val kodein by lazy { (activity?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: LoginViewModelFactory by instance()
    lateinit var mViewModel: LoginViewModel
    var isEditingMode = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!::mBinding.isInitialized) {
            mViewModel =
                ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
            mBinding = AadharDetailsFragmentBinding.inflate(inflater, container, false).apply {
                clickHandler = ClickHandler()
            }
        }
        setupObserver()
        initToolbar(
            mBinding.tToolbar,
            getString(R.string.aadhar_details),
            getString(R.string.enter_otp_msg)
        )

        setUpUI()

        mBinding.tToolbar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        return mBinding.root
    }


    private fun setUpUI() {
        if (arguments?.get(getString(R.string.aadhar_details)) != null) {
            var aaadharDetails =
                arguments?.get(getString(R.string.aadhar_details)) as UserAadharDetails
            isEditingMode = false
            mBinding.tToolbar.tvSubTitle.text =
                Html.fromHtml("You are viewing your's submitted " + "<font color=#0068FF>" + "aadhar details." + "</font>" + ".")
            mBinding.frontImage = aaadharDetails.aadharFrontImage?.showImage()
            mBinding.backImage = aaadharDetails.aadharBackImage?.showImage()
            mBinding.eAadharNo.isEnabled = false
            mBinding.eAadharNo.setText(aaadharDetails.aadharNumber.toString())

        } else {
            mBinding.tToolbar.tvSubTitle.text =
                Html.fromHtml("Let us know your " + "<font color=#0068FF>" + "aadhar details to identify the identity." + "</font>" + ".")

            (requireActivity() as AuthHandlerActivity).listener = this.ClickHandler()
            mViewModel.profileParams =
                arguments?.get(getString(R.string.profile_params)) as ProfileParams

        }

    }

    inner class ClickHandler : AuthHandlerActivity.onClickListeners {


        fun onBack() {
            findNavController().navigateUp()
        }

        fun clickAaadharFrontImage() {
            if (isEditingMode) {
                runWithPermissions(
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE

                ) {
                    ImagePicker.with(this@AaadharDetailsFragment)
                        .crop()
                        .compress(1024)
                        .maxResultSize(
                            1080,
                            1080
                        )
                        .start(100)

                }
            }


        }

        fun clickAaadharBackImage() {
            if (isEditingMode) {
                runWithPermissions(
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE

                ) {
                    ImagePicker.with(this@AaadharDetailsFragment)
                        .crop()
                        .compress(1024)
                        .maxResultSize(
                            1080,
                            1080
                        )
                        .start(101)
                }
            }


        }

        override fun onClickNext() {
            if (mViewModel.profileParams.aadharFrontImage == null) {
                showToast(getString(R.string.aadhar_image_front_pic_error))
            } else if (mViewModel.profileParams.aadharBackImage == null) {
                showToast(getString(R.string.aadhar_image_back_pic_error))
            } else if (mBinding.eAadharNo.text.toString().isEmpty()) {
                showToast(getString(R.string.aadhar_no_empty_error))
            } else if (mBinding.eAadharNo.text.toString().trim().length < 14) {
                showToast(getString(R.string.aadhar_no_valid_error))
            } else {
                mViewModel.profileParams.aadharNumber = mBinding.eAadharNo.text.toString()
                mViewModel.addProfile()


            }
        }


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

                val fileUri = Uri.fromFile(compressedImageFile!!)
                if (requestCode == 100) mBinding.ivImageFront.setImageURI(fileUri) else mBinding.ivImageBack.setImageURI(
                    fileUri
                )

                if (requestCode == 100) {
                    mViewModel.profileParams.aadharFrontImage =
                        MultipartBody.Part.createFormData(
                            "aadharFrontImage",
                            compressedImageFile?.name,
                            RequestBody.create(
                                "image/*".toMediaTypeOrNull(),
                                compressedImageFile!!

                            )
                        )
                } else {
                    mViewModel.profileParams.aadharBackImage =
                        MultipartBody.Part.createFormData(
                            "aadharBackImage",
                            compressedImageFile?.name,
                            RequestBody.create(
                                "image/*".toMediaTypeOrNull(),
                                compressedImageFile!!

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

    private fun setupObserver() {

        mViewModel.apply {
            data.observe(viewLifecycleOwner, Observer {
                Prefs.init().isProfileCompleted = "1"
                showToast("Profile Details Submitted Successfully, Welcome to ${getString(R.string.app_name)}")
                startActivity(Intent(requireActivity(), MainActivity::class.java))
                (requireActivity() as AuthHandlerActivity).finish()

            })
            showLoading.observe(viewLifecycleOwner, Observer {
                if (it == true) {
                    showProgress()
                } else hideProgress()
            })

            showMessage.observe(viewLifecycleOwner, Observer {
                hideProgress()
                if (!it.isNullOrEmpty()) {
                    showToast(it)
                }

            })
        }

    }


}