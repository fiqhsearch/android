package com.fiqhsearcher.screen.browse

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fiqhsearcher.R
import com.fiqhsearcher.components.PageTitle
import com.fiqhsearcher.data.Issue
import com.fiqhsearcher.fiqh.Madhhab
import com.fiqhsearcher.screen.browse.add.ToggleIcon
import com.fiqhsearcher.screen.search.SupabaseViewModel

@Composable
fun DisplayIssue(issue: Issue) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Divider(
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
            modifier = Modifier.padding(horizontal = 10.dp)
        )
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ToggleIcon(
                    expanded = expanded,
                    onIconClick = { expanded = !expanded }
                )
                Text(
                    text = issue.question,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    fontSize = 22.sp
                )
            }
            AnimatedVisibility(
                visible = expanded,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "الجواب",
                        modifier = Modifier.padding(10.dp),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = issue.answer,
                        modifier = Modifier.padding(10.dp),
                        fontSize = 20.sp
                    )
                    Text(
                        text = "الدليل",
                        modifier = Modifier.padding(10.dp),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = issue.proof,
                        modifier = Modifier.padding(10.dp),
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BrowseTopic(
    madhhab: Madhhab,
    section: Int,
    topic: Int,
    navigator: NavController,
    supabase: SupabaseViewModel = hiltViewModel()
) {
    val issuesL by supabase.getIssues(madhhab, section, topic).collectAsState(initial = null)
    if (issuesL == null) {
        Searching()
    } else {
        val issues = issuesL!!
        Column(modifier = Modifier.fillMaxSize()) {
            AddItemButton(stringResource(R.string.add_new_issue)) {
                navigator.navigate("newIssue/${section}/${topic}")
            }
            if (issues.isEmpty()) {
                NothingHere(navigator = navigator)
            } else {
                PageTitle(text = "المسائل", padding = 20.dp)
                LazyColumn(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxSize()
                ) {
                    items(issues) {
                        DisplayIssue(issue = it)
                    }
                }
            }
        }
    }
}
