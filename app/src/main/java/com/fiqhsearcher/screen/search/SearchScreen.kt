package com.fiqhsearcher.screen.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fiqhsearcher.R
import com.fiqhsearcher.components.PageTitle
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
                Image(
                    modifier = Modifier
                        .padding(10.dp)
                        .size(128.dp),
                    painter = painterResource(R.drawable.broken_magnifying_glass),
                    contentDescription = "No results found"
                )
                PageTitle(
                    text = "لم أجد شيءًا كهذا من قبل",
                    lineHeight = 45.sp
                )
                Text(
                    text = "لعلك تجد شيءًا إن استخدمت كلماتٍ أقل؟",
                    fontWeight = FontWeight.Medium,
                    fontSize = 27.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(15.dp)
                        .fillMaxWidth(),
                    lineHeight = 45.sp
                )
                FilledTonalButton(
                    onClick = { navigator.navigateUp() },
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text("العودة")
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "عدد النتائج: ${results.size}",
                    fontWeight = FontWeight.Medium,
                    fontSize = 27.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                )
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
}