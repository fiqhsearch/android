package com.fiqhsearcher.screen.browse

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.fiqhsearcher.R
import com.fiqhsearcher.components.PageTitle
import com.fiqhsearcher.preferences.PreferencesViewModel
import com.fiqhsearcher.screen.search.SupabaseViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BrowseAllSections(
    navigator: NavHostController,
    preferences: PreferencesViewModel = hiltViewModel(),
    supabase: SupabaseViewModel = hiltViewModel()
) {
    val madhab by preferences.madhab.collectAsState()
    val list by supabase.getSections(madhab).collectAsState(initial = null)
    if (list == null) {
        Searching()
    } else {
        Column {
            val sections = list!!
            AddItemButton(stringResource(id = R.string.add_new_section)) {
                navigator.navigate("newSection")
            }
            if (sections.isEmpty()) {
                NothingHere(navigator = navigator)
            } else {
                PageTitle(text = "الأقسام الفقهية", padding = 20.dp)
                androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.padding(horizontal = 10.dp)
                ) {
                    items(sections) {
                        BrowseItem(
                            text = it.name,
                            onClick = { navigator.navigate("browse/$madhab/${it.id}") }
                        )
                    }
                }
            }
        }
    }
}
