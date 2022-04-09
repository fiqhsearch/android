package com.fiqhsearcher.screen.home

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fiqhsearcher.R
import com.fiqhsearcher.R.drawable.magnifying_glass
import com.fiqhsearcher.R.string.home_header
import com.fiqhsearcher.R.string.home_subtitle
import com.fiqhsearcher.components.PageTitle

@Composable
fun HomeScreen(
    navigator: NavController
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        SettingsIcon(navigator)
        ScreenHead()

        SearchArea(navigator)
    }
}

@Composable
private fun SearchArea(navigator: NavController) {
    var query by rememberSaveable { mutableStateOf("") }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 15.dp)
    ) {
        var searchOption by rememberSaveable { mutableStateOf(SearchOption.QUESTIONS) }
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {

            SearchBar(
                value = query,
                onValueChange = { query = it },
                onSubmit = {
                    navigator.navigate("search/$query/$searchOption")
                }
            )
        }
        SearchOptionList(
            selected = searchOption,
            onSelected = { searchOption = it }
        )
        SearchButton(enabled = query.isNotEmpty()) {
            navigator.navigate("search/$query/$searchOption")
        }
        Spacer(modifier = Modifier.padding(2.dp))
        SeeAllQuestions {
            navigator.navigate("browseAllSections")
        }
    }
}

@Composable
private fun ScreenHead() {
    MagnifyingGlass()
    PageTitle(text = stringResource(id = home_header))
    Text(
        text = stringResource(id = home_subtitle),
        modifier = Modifier
            .padding(10.dp)
            .width(250.dp),
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Center,
        fontSize = 16.sp
    )
}

@Composable
private fun SettingsIcon(navigator: NavController) {
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
}

enum class SearchOption(val column: String, @StringRes val nameR: Int) {
    QUESTIONS("question", R.string.questions),
    ANSWERS("answer", R.string.answers),
    PROOFS("proof", R.string.proofs)
}

val Options = SearchOption.values().also { it.reverse() }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchOptionList(
    selected: SearchOption,
    onSelected: (SearchOption) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        for (option in Options) {
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(option.nameR),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                RadioButton(
                    selected = option == selected,
                    onClick = { onSelected(option) }
                )
            }
        }
    }
}

@Composable
private fun MagnifyingGlass() {
    Image(
        modifier = Modifier
            .padding(10.dp)
            .size(64.dp),
        painter = painterResource(magnifying_glass),
        contentDescription = "Fiqh Searcher"
    )
}
