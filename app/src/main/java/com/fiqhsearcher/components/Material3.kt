package com.fiqhsearcher.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ContentAlpha
import androidx.compose.material.SwitchDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver

/**
 * A switch component that uses the Material 3 colors of the
 * currently selected theme
 */
@Composable
fun Switch(
    modifier: Modifier = Modifier,
    checked: Boolean,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onCheckedChange: (Boolean) -> Unit,
    checkedThumbColor: Color = MaterialTheme.colorScheme.primary,
    checkedTrackColor: Color = checkedThumbColor,
    checkedTrackAlpha: Float = 0.54f,
    uncheckedThumbColor: Color = Color(100, 100, 100),
    uncheckedTrackColor: Color = MaterialTheme.colorScheme.onSurface,
    uncheckedTrackAlpha: Float = 0.38f,
    disabledCheckedThumbColor: Color = checkedThumbColor
        .copy(alpha = ContentAlpha.disabled)
        .compositeOver(MaterialTheme.colorScheme.surface),
    disabledCheckedTrackColor: Color = checkedTrackColor
        .copy(alpha = ContentAlpha.disabled)
        .compositeOver(MaterialTheme.colorScheme.surface),
    disabledUncheckedThumbColor: Color = uncheckedThumbColor
        .copy(alpha = ContentAlpha.disabled)
        .compositeOver(MaterialTheme.colorScheme.surface),
    disabledUncheckedTrackColor: Color = uncheckedTrackColor
        .copy(alpha = ContentAlpha.disabled)
        .compositeOver(MaterialTheme.colorScheme.surface)

) {
    androidx.compose.material.Switch(
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = SwitchDefaults.colors(
            checkedThumbColor = checkedThumbColor,
            checkedTrackColor = checkedTrackColor,
            checkedTrackAlpha = checkedTrackAlpha,
            uncheckedTrackAlpha = uncheckedTrackAlpha,
            uncheckedThumbColor = uncheckedThumbColor,
            uncheckedTrackColor = uncheckedTrackColor,
            disabledCheckedThumbColor = disabledCheckedThumbColor,
            disabledCheckedTrackColor = disabledCheckedTrackColor,
            disabledUncheckedThumbColor = disabledUncheckedThumbColor,
            disabledUncheckedTrackColor = disabledUncheckedTrackColor
        )
    )
}