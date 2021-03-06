package com.fiqhsearcher.screen.browse

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fiqhsearcher.R
import com.fiqhsearcher.components.PageTitle
import com.fiqhsearcher.screen.login.LoginViewModel

@Composable
fun Searching() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun NothingHere(navigator: NavController) {
    Column(
        modifier = Modifier
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PageTitle(
            text = stringResource(id = R.string.nothing_here),
            padding = 5.dp
        )

        Text(
            text = ":(",
            fontWeight = FontWeight.Medium,
            fontSize = 27.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
        )
        FilledTonalButton(
            onClick = { navigator.navigateUp() },
            modifier = Modifier.padding(10.dp)
        ) {
            Text("العودة")
        }
    }
}

@Composable
fun AddItemButton(
    text: String,
    auth: LoginViewModel = hiltViewModel(),
    onClick: () -> Unit,
) {
    val user by auth.user.collectAsState()
    if (user != null) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Add, contentDescription = "New",
                    modifier = Modifier.padding(5.dp)
                )
                Text(
                    text = text,
                    modifier = Modifier.padding(5.dp),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseItem(text: String, onClick: () -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(5.dp),
        modifier = Modifier
            .padding(10.dp)
            .heightIn(120.dp),
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            width = 0.75.dp,
            color = MaterialTheme.colorScheme.inverseSurface.copy(alpha = 0.25f)
        )
    ) {
        Column(
            modifier = Modifier
                .heightIn(120.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                modifier = Modifier
                    .padding(10.dp),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}