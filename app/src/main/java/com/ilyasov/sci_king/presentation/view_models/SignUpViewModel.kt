package com.ilyasov.sci_king.presentation.view_models

import android.content.Context
import android.content.SharedPreferences
import android.util.Patterns
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.ilyasov.sci_king.R
import com.ilyasov.sci_king.util.Constants.Companion.APP_PREFERENCES
import com.ilyasov.sci_king.util.Constants.Companion.IS_REGISTERED

class SignUpViewModel(private val context: Context) : View.OnClickListener{
    private val mSettings: SharedPreferences by lazy {
        context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
    }
    private val mAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private lateinit var progressBar: ProgressBar
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonSignUp: Button
    private lateinit var textViewLogin: TextView

    fun onViewCreated(view: View) {
        editTextEmail = view.findViewById(R.id.editTextEmail)
        editTextPassword = view.findViewById(R.id.editTextPassword)
        progressBar = view.findViewById(R.id.progressbar)
        buttonSignUp = view.findViewById(R.id.buttonSignUp)
        textViewLogin = view.findViewById(R.id.textViewLogin)
        buttonSignUp.setOnClickListener(this)
        textViewLogin.setOnClickListener(this)
    }

    private fun registerUser() {
        val email = editTextEmail.text.toString().trim { it <= ' ' }
        val password: String = editTextPassword.text.toString().trim { it <= ' ' }
        if (email.isEmpty()) {
            editTextEmail.error = "Email is required"
            editTextEmail.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.error = "Please enter a valid email"
            editTextEmail.requestFocus()
            return
        }
        if (password.isEmpty()) {
            editTextPassword.error = "Password is required"
            editTextPassword.requestFocus()
            return
        }
        if (password.length < 6) {
            editTextPassword.error = "Minimum length of password should be 6"
            editTextPassword.requestFocus()
            return
        }
        progressBar.visibility = View.VISIBLE
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    with (mSettings.edit()) {
                        putBoolean(IS_REGISTERED, true)
                        apply()
                    }
                    TODO("open SearchFragment")
                } else {
                    if (task.exception is FirebaseAuthUserCollisionException) {
                        Toast.makeText(
                            context,
                            "You are already registered",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            task.exception!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.buttonSignUp -> registerUser()
            R.id.textViewLogin -> {
                TODO("popBackStack or return to previous View with fragment manager or NavGraph")
            }
        }
    }
}