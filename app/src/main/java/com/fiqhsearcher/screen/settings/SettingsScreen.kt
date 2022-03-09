@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")

package com.fiqhsearcher.screen.settings

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.edit
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fiqhsearcher.Madhab
import com.fiqhsearcher.R
import com.fiqhsearcher.R.drawable.*
import com.fiqhsearcher.components.Switch
import com.fiqhsearcher.components.activity
import com.fiqhsearcher.preferences.DARK_THEME
import com.fiqhsearcher.preferences.MADHAB
import com.fiqhsearcher.preferences.PreferencesViewModel
import com.fiqhsearcher.preferences.dataStore
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    navigator: NavController,
    preferences: PreferencesViewModel = hiltViewModel(),
) {
    val scope = rememberCoroutineScope()
    val dataStore = LocalContext.current.dataStore
    val darkTheme by preferences.darkTheme.collectAsState()
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(id = R.string.settings),
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            textAlign = TextAlign.Center
        )
        Divider(modifier = Modifier.padding(horizontal = 20.dp))
        DisplayUserProfile(
            navigator = navigator,
            modifier = Modifier
                .align(End)
                .padding(10.dp)
        )
        SwitchWithLabel(
            checked = darkTheme,
            onCheckedChange = { v -> scope.launch { dataStore.edit { it[DARK_THEME] = v } } },
            label = stringResource(id = R.string.night_read)
        )
        val selectedMadhab by preferences.madhab.collectAsState()
        CategoryText(
            text = stringResource(id = R.string.my_madhab)
        )
        MadhabSelector(
            selected = selectedMadhab,
            onSelected = { v -> scope.launch { dataStore.edit { it[MADHAB] = v } } }
        )
        CategoryText(
            text = stringResource(R.string.contact_us)
        )
        ContactUs()
    }
}

@Composable
private fun ColumnScope.ContactUs() {
    val activity = LocalContext.current.activity
    SocialMediaIcon(
        onClick = { activity?.openFacebook() },
        icon = facebook,
        description = stringResource(id = R.string.facebook)
    )
    SocialMediaIcon(
        onClick = { activity?.openTwitter() },
        icon = twitter,
        description = stringResource(id = R.string.twitter)
    )
    SocialMediaIcon(
        onClick = { activity?.openTelegram() },
        icon = telegram,
        description = stringResource(id = R.string.telegram)
    )
}

@Composable
private fun ColumnScope.SocialMediaIcon(
    onClick: () -> Unit,
    @DrawableRes icon: Int,
    description: String
) {
    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .align(End)
            .clip(RoundedCornerShape(4.dp))
            .clickable { onClick() }
            .fillMaxWidth(),
    ) {
        Text(
            text = description,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp
        )
        IconButton(onClick = onClick) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = description
            )
        }
    }
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun MadhabSelector(
    selected: Madhab,
    onSelected: (Int) -> Unit
) {
    for (madhab in Madhab.values) {
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(madhab.nameResource),
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp
            )
            RadioButton(
                selected = madhab == selected,
                onClick = { onSelected(madhab.ordinal) }
            )
        }
    }
}

@Composable
private fun CategoryText(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp),
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Right,
        fontSize = 20.sp,
        color = MaterialTheme.colorScheme.onPrimaryContainer
    )
}

@Composable
private fun SwitchWithLabel(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Switch(
            modifier = Modifier.padding(
                horizontal = 11.dp,
                vertical = 5.dp
            ),
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Text(
            text = label,
            modifier = Modifier.padding(
                horizontal = 11.dp,
                vertical = 5.dp
            ),
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp
        )
    }
}
