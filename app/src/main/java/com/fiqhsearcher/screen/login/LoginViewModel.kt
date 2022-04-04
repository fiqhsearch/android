package com.fiqhsearcher.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiqhsearcher.supabase.SupabaseClient
import dagger.hilt.android.lifecycle.HiltViewModel
import io.supabase.gotrue.type.AuthChangeEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    client: SupabaseClient
) : ViewModel() {

    private val auth = client.auth
    private val _user = MutableStateFlow(auth.currentUser)
    val user = _user.asStateFlow()

    init {
        auth.on(AuthChangeEvent.SIGNED_IN) {
            _user.value = it?.user
        }
        auth.on(AuthChangeEvent.SIGNED_OUT) {
            _user.value = null
        }
        auth.recoverSession()
    }

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit = {},
        onFail: (Exception) -> Unit = {},
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                auth.signIn(email, password)
                viewModelScope.launch(Dispatchers.Main) {
                    onSuccess()
                }
            } catch (e: Exception) {
                viewModelScope.launch(Dispatchers.Main) {
                    onFail(e)
                    e.printStackTrace()
                }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                auth.signOut()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}