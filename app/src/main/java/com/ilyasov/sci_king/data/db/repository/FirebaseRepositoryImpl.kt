package com.ilyasov.sci_king.data.db.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.google.gson.Gson
import com.ilyasov.sci_king.domain.entity.SciArticle
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val fireStore: FirebaseStorage,
    private val gson: Gson,
) : FirebaseRepository {
    override fun signInUser(email: String, password: String): Task<AuthResult> =
        firebaseAuth.signInWithEmailAndPassword(email, password)

    override fun signUpUser(email: String, password: String): Task<AuthResult> =
        firebaseAuth.createUserWithEmailAndPassword(email, password)

    override fun uploadToCloud(sciArticle: SciArticle): UploadTask {
        val relPath = "/${firebaseAuth.currentUser!!.uid}/${sciArticle.id}.txt"
        val data = gson.toJson(sciArticle).toByteArray()
        val textRef = fireStore.reference.child(relPath)
        return textRef.putBytes(data)
    }

    override fun getCurrentUser(): FirebaseUser? = firebaseAuth.currentUser

    override fun logOut() = firebaseAuth.signOut()
}