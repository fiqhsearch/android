@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")

package com.fiqhsearcher.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.fiqhsearcher.R.drawable.magnifying_glass
import com.fiqhsearcher.R.string.home_header
import com.fiqhsearcher.R.string.home_subtitle
import com.fiqhsearcher.preferences.PreferencesViewModel
import com.fiqhsearcher.preferences.dataStore

@Composable
fun HomeScreen(
    preferences: PreferencesViewModel = hiltViewModel(),
    navigator: NavController
) {
    val darkTheme by preferences.darkTheme.collectAsState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { navigator.navigate("settings") }) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Settings"
                )
            }
        }
//        TopRow(
//            onSelectMadhab = {  },
//            darkTheme = darkTheme,
//            onToggleTheme = { value ->
//                scope.launch { store.edit { it[DARK_THEME] = value } }
//            }
//        )
        var query by remember { mutableStateOf("") }
        MagnifyingGlass()
        Text(
            text = stringResource(id = home_header),
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 30.sp
        )
        Text(
            text = stringResource(id = home_subtitle),
            modifier = Modifier
                .padding(10.dp)
                .width(250.dp),
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
}

//@Composable
//@OptIn(ExperimentalAnimationApi::class)
//private fun TopRow(
//    onSelectMadhab: () -> Unit,
//) {
//        AnimatedContent(targetState = if (darkTheme) moon else sun) {
//            IconButton(onClick = { onToggleTheme(!darkTheme) }) {
//                Icon(
//                    painter = painterResource(id = it),
//                    contentDescription = "Toggle theme"
//                )
//            }
//        }
//
//        IconButton(onClick = onSelectMadhab) {
//            Icon(
//                imageVector = Icons.Filled.Menu,
//                contentDescription = "Select madhab"
//            )
//        }
//    }
//}

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
