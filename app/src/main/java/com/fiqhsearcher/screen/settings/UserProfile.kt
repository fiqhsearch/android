package com.fiqhsearcher.screen.settings

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fiqhsearcher.R
import com.fiqhsearcher.screen.login.LoginViewModel

@Composable
fun DisplayUserProfile(
    modifier: Modifier = Modifier,
    navigator: NavController,
    login: LoginViewModel = hiltViewModel(),
) {
    val user by login.user.collectAsState()
    if (user == null) {
        Text(
            text = stringResource(id = R.string.click_to_sign_in),
            modifier = modifier.clickable { navigator.navigate("login") },
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp
        )
        return
    } else {
        val user = user ?: return
        Text(
            text = user.displayName ?: "No Name",
            modifier = modifier,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            textAlign = TextAlign.Right
        )
    }
}