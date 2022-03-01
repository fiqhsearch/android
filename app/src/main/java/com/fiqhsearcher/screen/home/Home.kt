@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")

package com.fiqhsearcher.screen.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.edit
import com.fiqhsearcher.Madhab
import com.fiqhsearcher.R
import com.fiqhsearcher.R.drawable.*
import com.fiqhsearcher.R.string.*
import com.fiqhsearcher.prefs.DARK_THEME
import com.fiqhsearcher.prefs.MADHAB
import com.fiqhsearcher.prefs.dataStore
import com.fiqhsearcher.ui.components.SearchTextBar
import com.fiqhsearcher.ui.components.Title
import kotlinx.coroutines.launch

@Composable
fun Home(darkTheme: Boolean, madhab: Madhab) {
    var displaySelector by remember { mutableStateOf(false) }
    val store = LocalContext.current.dataStore
    val scope = rememberCoroutineScope()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        TopRow(
            onSelectMadhab = { displaySelector = true },
            darkTheme = darkTheme,
            onToggleTheme = { value ->
                scope.launch { store.edit { it[DARK_THEME] = value } }
            }
        )
        var query by remember { mutableStateOf("") }
        MagnifyingGlass()
        Text(
            text = stringResource(id = home_header),
            modifier = Modifier.padding(10.dp).fillMaxWidth(),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 30.sp
        )
        Text(
            text = stringResource(id = home_subtitle),
            modifier = Modifier.padding(10.dp).width(250.dp),
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            fontSize = 16.sp
        )
        SearchBar(
            value = query,
            onValueChange = { query = it },
            darkTheme = darkTheme,
            onSubmit = {}
        )
        SearchButton {}
    }
    MadhabSelector(
        expanded = displaySelector,
        onDismissRequest = { displaySelector = false },
        selectedMadhab = madhab,
        onSelectMadhab = { m ->
            scope.launch {
                store.edit { it[MADHAB] = m.ordinal }
            }
        }
    )
}

@Composable
private fun MadhabSelector(
    expanded: Boolean,
    onSelectMadhab: (Madhab) -> Unit,
    onDismissRequest: () -> Unit,
    selectedMadhab: Madhab
) {
    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopEnd)
    ) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest
        ) {
            for (madhab in Madhab.values) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(id = madhab.nameResource),
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Right,
                            fontSize = 15.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    onClick = { onSelectMadhab(madhab) },
                    trailingIcon = {
                        AnimatedVisibility(visible = selectedMadhab == madhab) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun TopRow(
    onSelectMadhab: () -> Unit,
    darkTheme: Boolean = true,
    onToggleTheme: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        AnimatedContent(targetState = if (darkTheme) moon else sun) {
            IconButton(onClick = { onToggleTheme(!darkTheme) }) {
                Icon(
                    painter = painterResource(id = it),
                    contentDescription = "Toggle theme"
                )
            }
        }

        IconButton(onClick = onSelectMadhab) {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "Select madhab"
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchBar(
    darkTheme: Boolean,
    value: String,
    onValueChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current
    SearchTextBar(
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
        label = stringResource(R.string.search_label),
        keyboardActions = KeyboardActions(
            onSearch = {
                focusManager.clearFocus()
                keyboard?.hide()
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
private fun SearchButton(onClick: () -> Unit) {
    val keyboard = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    Button(
        modifier = Modifier.padding(10.dp),
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

@Composable
private fun MagnifyingGlass() {
    Image(
        modifier = Modifier
            .padding(top = 20.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)
            .size(64.dp),
        painter = painterResource(magnifying_glass),
        contentDescription = "Fiqh Searcher"
    )
}