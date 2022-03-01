package com.fiqhsearcher.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.fiqhsearcher.Madhab

@Composable
fun MadhabSelector(
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
                MadhabChoice(madhab, onSelectMadhab, selectedMadhab)
            }
        }
    }
}

@Composable
private fun MadhabChoice(
    madhab: Madhab,
    onSelectMadhab: (Madhab) -> Unit,
    selectedMadhab: Madhab
) {
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
