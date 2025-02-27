package com.example.furiyomi.repository


import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Registro de usuário
    suspend fun registerUser(
        email: String,
        password: String,
        name: String
    ): Boolean {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid

            if (uid != null) {
                val user = hashMapOf(
                    "uid" to uid,
                    "name" to name,
                    "email" to email,
                    "created_at" to System.currentTimeMillis(),
                    "photoUrl" to ""
                )

                firestore
                    .collection("users")
                    .document(uid)
                    .set(user)
                    .await()
            }

            true
        } catch (e: Exception) {
            Log.d("Email", "E-mail fornecido: $email")

            Log.e("AuthRepository", "Erro ao cadastrar: ${e.message}")
            false
        }
    }

    // Login com email e senha
    suspend fun loginUser(
        email: String,
        password: String
    ): Boolean {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            true
        } catch (e: Exception) {
            Log.e("AuthRepository", "Erro ao logar: ${e.message}")
            false
        }
    }

    suspend fun resetPassword(email: String): Boolean {
        return try {
            auth.sendPasswordResetEmail(email).await()
            true
        } catch (e: Exception) {
            Log.e("AuthRepository", "Erro ao enviar email de recuperação: ${e.message}")
            false
        }
    }

    suspend fun getUserName(): String? {
        return try {
            val uid = auth.currentUser?.uid

            if (uid != null) {
                val snapshot = firestore
                    .collection("users")
                    .document(uid)
                    .get().await()
                snapshot.getString("name")  // Retorna o nome salvo no Firestore
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(
                "AuthRepository",
                "Erro ao buscar nome do usuário: ${e.message}"
            )
            null
        }
    }

    suspend fun getUserPhoto(): String? {
        return try {
            val uid = auth.currentUser?.uid

            if (uid != null) {
                val snapshot = firestore
                    .collection("users")
                    .document(uid)
                    .get().await()
                snapshot.getString("photoUrl")  // Retorna o nome salvo no Firestore
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(
                "AuthRepository",
                "Erro ao buscar nome do usuário: ${e.message}"
            )
            null
        }
    }

    fun getGoogleSignInClient(context: Context): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(com.example.furiyomi.R.string.default_web_client_id))
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(context, gso)
    }

    suspend fun loginWithGoogle(idToken: String): Boolean {
        return try {
            var credential = GoogleAuthProvider.getCredential(idToken, null)
            var result = auth.signInWithCredential(credential).await()
            val user = result.user

            user?.let {
                val uid = it.uid
                val name = it.displayName ?: "User"
                val email = it.email ?: ""
                val photoUrl = it.photoUrl


                // Verifica se o usuário já existe no firestore antes de salvar
                val userRef = firestore.collection("users").document(uid)
                val snapshot = userRef.get().await()

                if (!snapshot.exists()) {
                    val userData = hashMapOf(
                        "uid" to uid,
                        "name" to name,
                        "email" to email,
                        "created_at" to System.currentTimeMillis(),
                        "photoUrl" to photoUrl
                    )
                    userRef.set(userData).await()
                }

                // Atualiando foto de perfil
                userRef.update("photoUrl", photoUrl).await()
            }
            true
        } catch (e: Exception) {
            Log.e("AuthRepository", "Erro ao fazer login com google: ${e.message}")
            false
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun isUserLogged(): Boolean {
        return auth.currentUser != null
    }

}