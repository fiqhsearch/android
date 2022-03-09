@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")

package com.fiqhsearcher.screen.login

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fiqhsearcher.R
import com.fiqhsearcher.components.TextBar
import com.fiqhsearcher.components.isValidEmail
import com.fiqhsearcher.preferences.PreferencesViewModel
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

@OptIn(ExperimentalComposeUiApi::class, androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navigator: NavController,
    preferences: PreferencesViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val darkTheme by preferences.darkTheme.collectAsState()
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var showPassword by rememberSaveable { mutableStateOf(false) }
    val visualTransformation = remember(showPassword) {
        if (showPassword) VisualTransformation.None
        else PasswordVisualTransformation()
    }
    val valid = rememberSaveable(email, password) {
        email.isValidEmail() && password.isNotEmpty()
    }
    var error by remember { mutableStateOf(false) }
    var errorString by remember { mutableStateOf<String?>(null) }

    val keyboard = LocalSoftwareKeyboardController.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.padding(10.dp))
        Text(
            text = stringResource(id = R.string.home_header),
            modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth(),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 30.sp
        )
        TextBar(
            darkTheme = darkTheme,
            value = email,
            onValueChange = {
                email = it
                error = false
            },
            surfaceModifier = Modifier
                .padding(vertical = 10.dp, horizontal = 20.dp)
                .fillMaxWidth(),
            textStyle = TextStyle.Default.copy(
                fontSize = 17.sp
            ),
            singleLine = true,
            modifier = Modifier.padding(10.dp),
            textAlign = TextAlign.Left,
            labelAlign = TextAlign.Right,
            label = stringResource(R.string.email),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                keyboardType = KeyboardType.Email
            )
        )
        TextBar(
            darkTheme = darkTheme,
            value = password,
            onValueChange = {
                password = it
                error = false
            },
            surfaceModifier = Modifier
                .padding(vertical = 10.dp, horizontal = 20.dp)
                .fillMaxWidth(),
            textStyle = TextStyle.Default.copy(
                fontSize = 17.sp
            ),
            textAlign = TextAlign.Left,
            labelAlign = TextAlign.Right,
            visualTransformation = visualTransformation,
            singleLine = true,
            modifier = Modifier.padding(10.dp),
            label = stringResource(R.string.password),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Go,
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(
                onGo = { keyboard?.hide() }
            )
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.show_password),
            )
            Checkbox(
                checked = showPassword,
                onCheckedChange = { showPassword = it },
            )
        }
        val context = LocalContext.current
        Button(
            onClick = {
                keyboard?.hide()
                loginViewModel.login(
                    email = email,
                    password = password,
                    onFail = {
                        error = true
                        errorString = context.mapToMessage(it)
                    },
                    onSuccess = {
                        navigator.navigate("settings") {
                            popUpTo("home")
                        }
                    }
                )
            },
            enabled = valid
        ) {
            Text(
                text = stringResource(R.string.login),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
        if (error && errorString != null) {
            Text(
                text = errorString!!,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.End,
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}

private fun Context.mapToMessage(exception: Exception): String {
    val id = when (exception) {
        is FirebaseAuthInvalidUserException -> R.string.invalid_user
        is FirebaseAuthInvalidCredentialsException -> R.string.wrong_password
        else -> R.string.error_occurred
    }
    return getString(id)
}
