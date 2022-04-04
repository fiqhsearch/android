package com.fiqhsearcher.screen.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fiqhsearcher.R.string.search_label
import com.fiqhsearcher.R.string.start_search
import com.fiqhsearcher.components.TextBar
import com.fiqhsearcher.screen.queryArg

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun SearchBar(
    darkTheme: Boolean,
    value: String,
    onValueChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current
    TextBar(
        darkTheme = darkTheme,
        value = value,
        onValueChange = onValueChange,
        surfaceModifier = Modifier
            .padding(20.dp)
            .fillMaxWidth(),
        textStyle = TextStyle.Default.copy(
            fontSize = 17.sp
        ),
        singleLine = true,
        modifier = Modifier.padding(10.dp),
        label = stringResource(search_label),
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
@Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
@Composable
fun SearchButton(enabled: Boolean, onClick: () -> Unit) {
    val keyboard = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    Button(
        modifier = Modifier.padding(10.dp),
        enabled = enabled,
        onClick = {
            keyboard?.hide()
            focusManager.clearFocus()
            onClick()
        }
    ) {
        Text(
            text = stringResource(start_search),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
