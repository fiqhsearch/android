package com.fiqhsearcher.screen.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {

    private val auth = Firebase.auth
    private val _user = MutableStateFlow(auth.currentUser)
    private val db = FirebaseDatabase.getInstance().reference
    val user = _user.asStateFlow()

    fun login(
        email: String,
        password: String,
        onFail: (Exception) -> Unit = {},
        onSuccess: () -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _user.value = it.user
                onSuccess()
            }
            .addOnFailureListener {
                Log.d("FirebaseAuth", "Failed to authenticate with email and password", it)
                onFail(it)
            }
    }

    private fun loginWithCredential(credential: AuthCredential) {
        auth.signInWithCredential(credential).addOnSuccessListener {
            _user.value = it.user
        }.addOnFailureListener { it.printStackTrace() }
    }

    fun signOut() {
        auth.signOut()
        _user.value = null
    }
}

//    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//        .requestIdToken(context.getString(R.string.default_web_client_id))
//        .requestEmail()
//        .build()
//
//     Configure Google Sign In
//    private val googleSignInClient = GoogleSignIn.getClient(context, gso)
//    fun googleSignInIntent() = googleSignInClient.signInIntent
//
//    @Composable
//    fun signInWithGoogle() = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
//        try {
//            val account = task.getResult(ApiException::class.java)!!
//            val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
//            loginWithCredential(credential)
//        } catch (e: ApiException) {
//            Log.e("TAG", "Google sign in failed", e)
//        }
//    }
