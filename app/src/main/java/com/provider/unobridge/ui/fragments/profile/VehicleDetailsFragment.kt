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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.provider.unobridge.R
import com.provider.unobridge.base.ScopedFragment
import com.provider.unobridge.data.ProfileParams
import com.provider.unobridge.data.responeClasses.UserDriverLicenseDetails
import com.provider.unobridge.databinding.VehicleDetailsFragmentBinding
import com.provider.unobridge.ext.showImage
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
import java.util.regex.Pattern

class VehicleDetailsFragment : ScopedFragment(), KodeinAware {
    lateinit var mBinding: VehicleDetailsFragmentBinding
    override val kodein by lazy { (activity?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: LoginViewModelFactory by instance()
    lateinit var mViewModel: LoginViewModel
    var isEditingMode = true


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!::mBinding.isInitialized) {
            mViewModel =
                ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
            mBinding = VehicleDetailsFragmentBinding.inflate(inflater, container, false).apply {
                clickHandler = ClickHandler()
            }
        }

        initToolbar(
            mBinding.tToolbar,
            getString(R.string.vehicle_details),
            getString(R.string.enter_otp_msg)
        )

        setUpUI()

        mBinding.tToolbar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
        return mBinding.root
    }

    private fun setUpUI() {
        if (arguments?.get(getString(R.string.vehicle_details)) != null) {
            val vehicleDetails =
                arguments?.get(getString(R.string.vehicle_details)) as UserDriverLicenseDetails
            isEditingMode = false
            mBinding.tToolbar.tvSubTitle.text =
                Html.fromHtml("You are viewing your's submitted " + "<font color=#0068FF>" + "Driver details." + "</font>" + ".")
            mBinding.frontImage = vehicleDetails.licenseFrontImage?.showImage()
            mBinding.backImage = vehicleDetails.licenseBackImage?.showImage()
            mBinding.etLicenseNo.setText(vehicleDetails.licenseNo.toString())
            mBinding.etLicenseNo.isEnabled = false

        } else {
            mBinding.tToolbar.tvSubTitle.text =
                Html.fromHtml("Let us know your " + "<font color=#0068FF>" + "vehicle details." + "</font>" + ".")

            (requireActivity() as AuthHandlerActivity).listener = this.ClickHandler()
            mViewModel.profileParams =
                arguments?.get(getString(R.string.profile_params)) as ProfileParams

        }

    }


    inner class ClickHandler : AuthHandlerActivity.onClickListeners {

        fun clickLicenseFrontImage() {
            if (isEditingMode) {
                runWithPermissions(
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE

                ) {
                    ImagePicker.with(this@VehicleDetailsFragment)
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

        fun clickLicenseBackImage() {
            if (isEditingMode) {
                runWithPermissions(
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE

                ) {
                    ImagePicker.with(this@VehicleDetailsFragment)
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

            if (mViewModel.profileParams.licenseFrontImage == null) {
                showToast(getString(R.string.license_image_front_pic_error))
            } else if (mViewModel.profileParams.licenseBackImage == null) {
                showToast(getString(R.string.license_image_back_pic_error))
            } else if (mBinding.etLicenseNo.text.toString().isEmpty()) {
                showToast(getString(R.string.license_no_empty_error))
            }/* else if (!isValidLicenseNo(mBinding.etLicenseNo.text.toString())) {
                showToast(getString(R.string.license_no_valid_error))
            }*/ else {
                mViewModel.profileParams.licenseNo = mBinding.etLicenseNo.text.toString()
                val bundle = Bundle()
                bundle.putParcelable(getString(R.string.profile_params), mViewModel.profileParams)


                findNavController().navigate(R.id.aadhar_details_fragment, bundle)


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

                val fileUri = Uri.fromFile(compressedImageFile!!)
                if (requestCode == 100) mBinding.ivImageLicenseFront.setImageURI(fileUri) else mBinding.ivImageLicenseBack.setImageURI(
                    fileUri
                )

                if (requestCode == 100) {
                    mViewModel.profileParams.licenseFrontImage =
                        MultipartBody.Part.createFormData(
                            "licenseFrontImage",
                            compressedImageFile?.name,
                            RequestBody.create(
                                "image/*".toMediaTypeOrNull(),
                                compressedImageFile!!

                            )
                        )
                } else {
                    mViewModel.profileParams.licenseBackImage =
                        MultipartBody.Part.createFormData(
                            "licenseBackImage",
                            compressedImageFile?.name,
                            RequestBody.create(
                                "image/*".toMediaTypeOrNull(),
                                compressedImageFile!!

                            )
                        )
                }

                mViewModel.profileParams.licenseFrontImage


            }
        }
    }

    private fun isValidLicenseNo(email: String): Boolean {
        return Pattern.compile("^[A-Z](?:\\d[- ]*){14}\$\n").matcher(email).matches()
    }


}