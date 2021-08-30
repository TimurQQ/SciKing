package com.ilyasov.sci_king.presentation.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.ilyasov.sci_king.BR
import com.ilyasov.sci_king.R
import com.ilyasov.sci_king.databinding.FragmentUserProfileBinding
import com.ilyasov.sci_king.presentation.fragments.base.BaseFragment
import com.ilyasov.sci_king.presentation.viewmodels.UserProfileViewModel
import com.ilyasov.sci_king.util.Constants.Companion.EMAIL_NOT_VERIFIED_TEXT
import com.ilyasov.sci_king.util.Constants.Companion.EMAIL_VERIFIED_TEXT
import com.ilyasov.sci_king.util.Constants.Companion.IMAGE_CHOOSER_TITLE_TEXT
import com.ilyasov.sci_king.util.Constants.Companion.LOAD_IMG_INTENT_TYPE
import com.ilyasov.sci_king.util.Constants.Companion.VERIFICATION_SEND_MSG
import com.ilyasov.sci_king.util.isVisible
import com.ilyasov.sci_king.util.showToast
import kotlinx.android.synthetic.main.fragment_user_profile.*
import java.io.IOException

class UserProfileFragment : BaseFragment(R.layout.fragment_user_profile) {
    private val viewModel: UserProfileViewModel by lazy { createViewModel {} }
    private lateinit var fragmentUserProfileBinding: FragmentUserProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        fragmentUserProfileBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_user_profile, container, false)
        fragmentUserProfileBinding.setVariable(BR.userProfileFragment, this)
        return fragmentUserProfileBinding.root
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        viewModel.loadUserInformation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadUserInformation()
        viewModel.errorStateLiveData.observe(viewLifecycleOwner) { error_message ->
            editTextDisplayName.apply { error = error_message; requestFocus() }
        }
        viewModel.callbackLiveData.observe(viewLifecycleOwner) { callback_message ->
            showToast(callback_message)
        }
        viewModel.loadingMutableLiveData.observe(viewLifecycleOwner) { visibility ->
            progressBar.isVisible(visibility)
        }
        viewModel.changeUserDisplayNameLiveData.observe(viewLifecycleOwner) { user_display_name ->
            editTextDisplayName.setText(user_display_name)
        }
        viewModel.emailVerificationLiveData.observe(viewLifecycleOwner) { isEmailVerified ->
            if (isEmailVerified) {
                textViewVerified.text = EMAIL_VERIFIED_TEXT
            } else {
                textViewVerified.text = EMAIL_NOT_VERIFIED_TEXT
                textViewVerified.setOnClickListener { onClickEmailVerification() }
            }
        }
        viewModel.imageLoadingLiveData.observe(viewLifecycleOwner) { request_options_and_photo_url ->
            Glide.with(this)
                .asBitmap()
                .load(request_options_and_photo_url.second)
                .apply(request_options_and_photo_url.first)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) {}
                    override fun onResourceReady(
                        resource: Bitmap, transition: Transition<in Bitmap>?,
                    ) = imgProfile.setImageBitmap(resource)
                })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.data != null) {
            val uriProfileImage: Uri = data.data!!
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(
                    requireActivity().contentResolver, uriProfileImage
                )
                imgProfile.setImageBitmap(bitmap)
                viewModel.uploadImageToFirebaseStorage(uriProfileImage)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun onClickEmailVerification() {
        viewModel.sendEmailVerification { showToast(VERIFICATION_SEND_MSG) }
    }

    fun onClickSave() = viewModel.saveUserInformation(editTextDisplayName.text.toString())

    fun onClickLogOut() {
        viewModel.logOut()
        findNavController().popBackStack(R.id.user_profile_nav_graph, true)
    }

    fun onClickProfileImage() = showImageChooser()

    private fun showImageChooser() {
        val intent = Intent()
        intent.type = LOAD_IMG_INTENT_TYPE
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, IMAGE_CHOOSER_TITLE_TEXT), CHOOSE_IMAGE)
    }

    companion object {
        private const val CHOOSE_IMAGE = 101
    }
}