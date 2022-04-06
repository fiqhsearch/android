package com.fiqhsearcher.screen.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fiqhsearcher.preferences.PreferencesViewModel
import com.fiqhsearcher.screen.browse.DisplayIssue
import com.fiqhsearcher.screen.browse.Searching
import com.fiqhsearcher.screen.home.SearchOption

@Composable
fun SearchScreen(
    navigator: NavController,
    query: String,
    searchOption: SearchOption,
    supabase: SupabaseViewModel = hiltViewModel(),
    pref: PreferencesViewModel = hiltViewModel()
) {
    val madhhab by pref.madhab.collectAsState()
    val resultsList by supabase.search(searchOption, query, madhhab).collectAsState(initial = null)
    if (resultsList == null) {
        Searching()
    } else {
        val results = resultsList!!
        if (results.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "لم أجد شيءًا كهذا من قبل",
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                )

                Text(
                    text = "لعلك تجد شيءًا إن استخدمت كلماتٍ أقل؟",
                    fontWeight = FontWeight.Medium,
                    fontSize = 27.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize()
            ) {
                items(results) {
                    DisplayIssue(issue = it)
                }
            }
        }
    }
}