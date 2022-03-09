package com.fiqhsearcher.components

import android.util.Patterns.EMAIL_ADDRESS
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import com.fiqhsearcher.ui.theme.DarkThemeColors
import com.fiqhsearcher.ui.theme.LightThemeColors
import kotlinx.coroutines.launch

@Composable
fun TextBar(
    darkTheme: Boolean,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    surfaceModifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    label: String = "",
    focusedBackgroundColor: Color = MaterialTheme.colorScheme.surface,
    focusedBorderColor: Color = MaterialTheme.colorScheme.inversePrimary,
    shape: Shape = RoundedCornerShape(5.dp),
    textStyle: TextStyle = LocalTextStyle.current,
    textColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    textAlign: TextAlign = TextAlign.Right,
    labelAlign: TextAlign = TextAlign.Right,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    cursorBrush: Brush = SolidColor(MaterialTheme.colorScheme.onSecondaryContainer),
) {
    val sc = if (darkTheme)
        DarkThemeColors.secondaryContainer
    else
        LightThemeColors.secondaryContainer
    val borderColor = remember(darkTheme) { Animatable(Color.Transparent) }
    val backgroundColor = remember(darkTheme) { Animatable(sc) }
    val scope = rememberCoroutineScope()
    Surface(
        modifier = surfaceModifier,
        shape = shape,
        color = backgroundColor.value,
        border = BorderStroke(2.dp, borderColor.value)
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .onFocusChanged {
                    scope.launch {
                        borderColor.animateTo(if (it.isFocused) focusedBorderColor else Color.Transparent)
                        backgroundColor.animateTo(if (it.isFocused) focusedBackgroundColor else sc)
                    }
                },
            interactionSource = interactionSource,
            singleLine = singleLine,
            maxLines = maxLines,
            visualTransformation = visualTransformation,
            onTextLayout = onTextLayout,
            readOnly = readOnly,
            enabled = enabled,
            textStyle = textStyle.copy(
                color = textColor,
                textAlign = textAlign,
                textDirection = TextDirection.Content
            ),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            cursorBrush = cursorBrush,
            decorationBox = {
                Column(verticalArrangement = Arrangement.Center) {
                    AnimatedVisibility(visible = value.isEmpty()) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = label,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.5f),
                            textAlign = labelAlign
                        )
                    }
                }
                it()
            }
        )
    }
}
fun String.isValidEmail(): Boolean {
    return isNotEmpty() && EMAIL_ADDRESS.matcher(this).matches()
}