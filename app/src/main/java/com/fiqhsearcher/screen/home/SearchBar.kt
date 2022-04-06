@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")

package com.fiqhsearcher.screen.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fiqhsearcher.R.string.*

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current
    com.fiqhsearcher.components.textfields.TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .padding(
                horizontal = 10.dp,
                vertical = 10.dp
            )
            .fillMaxWidth(),
//        textStyle = TextStyle.Default.copy(
//            fontSize = 17.sp,
//        ),
        singleLine = true,
        label = {
            Text(
                text = stringResource(search_label),
                style = LocalTextStyle.current,
                modifier = Modifier.fillMaxWidth()
            )
        },
        trailingIcon = {
            IconButton(
                onClick = onSubmit,
                enabled = value.isNotEmpty()
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search"
                )
            }
        },
        keyboardActions = KeyboardActions(
            onSearch = {
                focusManager.clearFocus()
                keyboard?.hide()
                if (value.isNotEmpty())
                    onSubmit()
            }
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        )
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchButton(enabled: Boolean, onClick: () -> Unit) {
    val keyboard = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    Button(
        enabled = enabled,
        onClick = {
            keyboard?.hide()
            focusManager.clearFocus()
            onClick()
        }
    ) {
        Text(
            text = stringResource(search),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SeeAllQuestions(onClick: () -> Unit) {
    val keyboard = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    Button(
        onClick = {
            keyboard?.hide()
            focusManager.clearFocus()
            onClick()
        }
    ) {
        Text(
            text = stringResource(start_search),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}
