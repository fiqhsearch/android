@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")

package com.fiqhsearcher.screen.login

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fiqhsearcher.R
import com.fiqhsearcher.components.PageTitle
import com.fiqhsearcher.components.textfields.TextField
import com.fiqhsearcher.components.textfields.isEmailValid

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navigator: NavController,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var showPassword by rememberSaveable { mutableStateOf(false) }
    val valid = rememberSaveable(email, password) {
        email.isEmailValid() && password.isNotEmpty()
    }
    var errorString by remember { mutableStateOf<String?>(null) }

    val keyboard = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.padding(10.dp))
        PageTitle(
            text = stringResource(id = R.string.home_header),
            padding = 15.dp
        )
        EmailField(
            email = email,
            onEmailChange = { email = it },
            removeError = { errorString = null }
        )
        PasswordField(
            password = password,
            onPasswordChange = { password = it },
            removeError = { errorString = null },
            showPassword = showPassword
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
        Button(
            onClick = {
                keyboard?.hide()
                loginViewModel.login(
                    email = email,
                    password = password,
                    onSuccess = {
                        navigator.navigate("settings") {
                            popUpTo("home")
                        }
                    },
                    onFail = {
                        errorString = context.getMessage(it)
                    },
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
        if (errorString != null) {
            Text(
                text = errorString!!,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun PasswordField(
    password: String,
    onPasswordChange: (String) -> Unit,
    removeError: () -> Unit,
    showPassword: Boolean,
) {
    val visualTransformation = remember(showPassword) {
        if (showPassword)
            VisualTransformation.None
        else
            PasswordVisualTransformation()
    }
    val keyboard = LocalSoftwareKeyboardController.current
    TextField(
        value = password,
        onValueChange = {
            onPasswordChange(it)
            removeError()
        },
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 20.dp)
            .fillMaxWidth(),
        visualTransformation = visualTransformation,
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(
            fontFamily = FontFamily.Default
        ),
        label = {
            Text(
                text = stringResource(R.string.password),
                textAlign = TextAlign.End,
                fontFamily = FontFamily.Default
            )
        },
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
}

@Composable
private fun EmailField(
    email: String,
    onEmailChange: (String) -> Unit,
    removeError: () -> Unit
) {
    TextField(
        value = email,
        onValueChange = {
            onEmailChange(it)
            removeError()
        },
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 20.dp)
            .fillMaxWidth(),
        textStyle = TextStyle.Default.copy(
            fontSize = 17.sp,
            fontFamily = FontFamily.Default
        ),
        singleLine = true,
        label = {
            Text(
                text = stringResource(R.string.email),
                style = LocalTextStyle.current.copy(
                    textDirection = TextDirection.Rtl,
                    fontFamily = FontFamily.Default
                )
            )
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            capitalization = KeyboardCapitalization.None,
            autoCorrect = false,
            keyboardType = KeyboardType.Email
        )
    )
}

private fun Context.getMessage(it: Exception): String? {
    return when (val m = it.message?.lowercase()) {
        "invalid login credentials" -> getString(R.string.invalid_user)
        else -> m
    }
}
