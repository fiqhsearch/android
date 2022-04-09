package com.fiqhsearcher.screen.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fiqhsearcher.R
import com.fiqhsearcher.screen.login.LoginViewModel
import io.supabase.gotrue.type.SupabaseUser

@Composable
fun DisplayUserProfile(
    modifier: Modifier = Modifier,
    navigator: NavController,
    login: LoginViewModel = hiltViewModel(),
) {
    val user by login.user.collectAsState()
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (user != null)
            SignOutButton(
                modifier = modifier,
                onClick = { login.signOut() }
            )
        UserName(user = user, modifier = modifier, navigator = navigator)
    }
}

@Composable
private fun SignOutButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    var displayConfirmationDialogue by remember { mutableStateOf(false) }
//    TextButton(
//        onClick = { displayConfirmationDialogue = true },
//        contentPadding = PaddingValues(0.dp),
//        modifier = Modifier.padding(2.dp)
//    ) {
    Text(
        text = stringResource(id = R.string.sign_out),
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) {
            displayConfirmationDialogue = true
        },
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.error,
    )
//    }
    if (displayConfirmationDialogue) {
        AlertDialog(
            modifier = Modifier.padding(20.dp),
            onDismissRequest = { displayConfirmationDialogue = false },
            title = {
                Text(
                    text = stringResource(id = R.string.are_you_sure),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Right,
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        displayConfirmationDialogue = false
                        onClick()
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.yes),
                        textAlign = TextAlign.Right,
                        fontSize = 18.sp,
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { displayConfirmationDialogue = false }
                ) {
                    Text(
                        text = stringResource(id = R.string.no),
                        textAlign = TextAlign.Right,
                        fontSize = 18.sp,
                    )
                }
            }
        )
    }
}

@Composable
private fun UserName(
    user: SupabaseUser?,
    modifier: Modifier = Modifier,
    navigator: NavController
) {
    if (user == null) {
        Text(
            text = stringResource(id = R.string.click_to_sign_in),
            modifier = modifier
                .clickable { navigator.navigate("login") }
                .fillMaxWidth(),
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            textAlign = TextAlign.Right
        )
    } else {
        val displayName = user.userMetadata.also { println(it) }
            .key["name"]?.toString() ?: user.email ?: "no name"
        Text(
            text = displayName,
            modifier = modifier,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            textAlign = TextAlign.Right,
        )
    }
}