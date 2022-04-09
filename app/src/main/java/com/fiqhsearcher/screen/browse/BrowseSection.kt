package com.fiqhsearcher.screen.browse

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fiqhsearcher.R
import com.fiqhsearcher.components.PageTitle
import com.fiqhsearcher.fiqh.Madhhab
import com.fiqhsearcher.screen.search.SupabaseViewModel
import com.google.accompanist.placeholder.material.placeholder

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BrowseSection(
    madhhab: Madhhab,
    navigator: NavController,
    id: Int,
    supabase: SupabaseViewModel = hiltViewModel()
) {
    val list by supabase.getTopics(madhhab, id).collectAsState(initial = null)
    if (list == null) {
        Searching()
    } else {
        Column {
            if (list != null) {
                AddItemButton(stringResource(id = R.string.add_new_topic)) {
                    navigator.navigate("newTopic/${id}")
                }
                val topics = list!!
                if (topics.isEmpty()) {
                    NothingHere(navigator = navigator)
                } else {
                    PageTitle(text = "الأبواب الفقهية", padding = 20.dp)
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.padding(10.dp)
                    ) {
                        items(topics) {
                            BrowseItem(text = it.name) {
                                navigator.navigate("topics/${madhhab}/${id}/${it.id}")
                            }
                        }
                    }
                }
            }
        }
    }
}