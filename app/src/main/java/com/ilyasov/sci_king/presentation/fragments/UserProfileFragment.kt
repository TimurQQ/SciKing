package com.ilyasov.sci_king.presentation.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.auth.FirebaseAuth
import com.ilyasov.sci_king.R
import com.ilyasov.sci_king.presentation.view_models.UserProfileViewModel
import com.ilyasov.sci_king.util.isVisible
import kotlinx.android.synthetic.main.fragment_user_profile.*
import java.io.IOException

class UserProfileFragment : Fragment(R.layout.fragment_user_profile) {
    private val viewModel by lazy { UserProfileViewModel() }

    var uriProfileImage: Uri? = null

    private val mAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        viewModel.loadUserInformation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadUserInformation()
        imageView.setOnClickListener { showImageChooser() }
        buttonSave.setOnClickListener { viewModel.saveUserInformation(editTextDisplayName.text.toString()) }
        buttonLogout.setOnClickListener {
            mAuth.signOut()
            findNavController().popBackStack(R.id.user_profile_nav_graph, true)
        }

        viewModel.errorStateLiveData.observe(viewLifecycleOwner, { error_message ->
            editTextDisplayName.apply {
                error = error_message
                requestFocus()
            }
        })

        viewModel.callbackLiveData.observe(viewLifecycleOwner, { callback_message ->
            Toast.makeText(
                context, callback_message, Toast.LENGTH_SHORT
            ).show()
        })

        viewModel.loadingMutableLiveData.observe(viewLifecycleOwner, { visibility ->
            progressBar.isVisible(visibility)
        })

        viewModel.changeUserDisplayNameLiveData.observe(viewLifecycleOwner, { user_display_name ->
            editTextDisplayName.setText(user_display_name)
        })

        viewModel.emailVerificationLiveData.observe(viewLifecycleOwner, { isEmailVerified ->
            if (isEmailVerified) {
                textViewVerified.text = "Email Verified"
            } else {
                textViewVerified.text = "Email Not Verified (Click to Verify)"
                textViewVerified.setOnClickListener {
                    mAuth.currentUser!!.sendEmailVerification().addOnCompleteListener {
                        Toast.makeText(
                            context, "Verification Email Sent", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })

        viewModel.imageLoadingLiveData.observe(viewLifecycleOwner, { request_options ->
            Glide.with(this)
                .asBitmap()
                .load(mAuth.currentUser!!.photoUrl.toString())
                .apply(request_options)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) {}
                    override fun onResourceReady(
                        resource: Bitmap, transition: Transition<in Bitmap>?
                    ) = imageView.setImageBitmap(resource)
                })
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.data != null) {
            uriProfileImage = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(
                    requireActivity().contentResolver,
                    uriProfileImage
                )
                imageView.setImageBitmap(bitmap)
                viewModel.uploadImageToFirebaseStorage(uriProfileImage)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun showImageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE)
    }

    companion object {
        private const val CHOOSE_IMAGE = 101
    }
}