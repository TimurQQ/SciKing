package com.ilyasov.sci_king.data.db.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.UploadTask
import com.ilyasov.sci_king.domain.entity.SciArticle

interface FirebaseRepository {
    fun signInUser(email: String, password: String): Task<AuthResult>
    fun signUpUser(email: String, password: String): Task<AuthResult>
    fun uploadToCloud(sciArticle: SciArticle): UploadTask
    fun getCurrentUser(): FirebaseUser?
    fun logOut()
}