/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fiqhsearcher.components.dropdown;

import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.node.Ref
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.DpOffset
import com.fiqhsearcher.components.textfields.Strings
import com.fiqhsearcher.components.textfields.getString
import kotlinx.coroutines.coroutineScope
import kotlin.math.max

/**
 * [Material Design Exposed Dropdown Menu](https://material.io/components/menus#exposed-dropdown-menu).
 *
 * Box for Exposed Dropdown Menu. Expected to contain [TextField] and
 * [ExposedDropdownMenuBoxScope.ExposedDropdownMenu] as a content.
 *
 * An example of read-only Exposed Dropdown Menu:
 *
 * @sample androidx.compose.material.samples.ExposedDropdownMenuSample
 *
 * An example of editable Exposed Dropdown Menu:
 *
 * @sample androidx.compose.material.samples.EditableExposedDropdownMenuSample
 *
 * @param expanded Whether Dropdown Menu should be expanded or not.
 * @param onExpandedChange Executes when the user clicks on the ExposedDropdownMenuBox.
 * @param modifier The modifier to apply to this layout
 * @param content The content to be displayed inside ExposedDropdownMenuBox.
 */
@Composable
fun ExposedDropdownMenuBox(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ExposedDropdownMenuBoxScope.() -> Unit
) {
    val density = LocalDensity.current
    val view = LocalView.current
    var width by remember { mutableStateOf(0) }
    var menuHeight by remember { mutableStateOf(0) }
    val verticalMarginInPx = with(density) { MenuVerticalMargin.roundToPx() }
    val coordinates = remember { Ref<LayoutCoordinates>() }

    val scope = remember(density, menuHeight, width) {
        object : ExposedDropdownMenuBoxScope {
            override fun Modifier.exposedDropdownSize(matchTextFieldWidth: Boolean): Modifier {
                return with(density) {
                    heightIn(max = menuHeight.toDp()).let {
                        if (matchTextFieldWidth) {
                            it.width(width.toDp())
                        } else it
                    }
                }
            }
        }
    }
    val focusRequester = remember { FocusRequester() }

    Box(
        modifier
            .onGloballyPositioned {
                width = it.size.width
                coordinates.value = it
                updateHeight(
                    view.rootView,
                    coordinates.value,
                    verticalMarginInPx
                ) { newHeight ->
                    menuHeight = newHeight
                }
            }
            .expandable(
                onExpandedChange = { onExpandedChange(!expanded) },
                menuLabel = getString(Strings.ExposedDropdownMenu)
            )
            .focusRequester(focusRequester)
    ) {
        scope.content()
    }

    SideEffect {
        if (expanded) focusRequester.requestFocus()
    }

    DisposableEffect(view) {
        val listener = OnGlobalLayoutListener(view) {
            // We want to recalculate the menu height on relayout - e.g. when keyboard shows up.
            updateHeight(
                view.rootView,
                coordinates.value,
                verticalMarginInPx
            ) { newHeight ->
                menuHeight = newHeight
            }
        }
        onDispose { listener.dispose() }
    }
}

/**
 * Subscribes to onGlobalLayout and correctly removes the callback when the View is detached.
 * Logic copied from AndroidPopup.android.kt.
 */
private class OnGlobalLayoutListener(
    private val view: View,
    private val onGlobalLayoutCallback: () -> Unit
) : View.OnAttachStateChangeListener, ViewTreeObserver.OnGlobalLayoutListener {
    private var isListeningToGlobalLayout = false

    init {
        view.addOnAttachStateChangeListener(this)
        registerOnGlobalLayoutListener()
    }

    override fun onViewAttachedToWindow(p0: View?) = registerOnGlobalLayoutListener()

    override fun onViewDetachedFromWindow(p0: View?) = unregisterOnGlobalLayoutListener()

    override fun onGlobalLayout() = onGlobalLayoutCallback()

    private fun registerOnGlobalLayoutListener() {
        if (isListeningToGlobalLayout || !view.isAttachedToWindow) return
        view.viewTreeObserver.addOnGlobalLayoutListener(this)
        isListeningToGlobalLayout = true
    }

    private fun unregisterOnGlobalLayoutListener() {
        if (!isListeningToGlobalLayout) return
        view.viewTreeObserver.removeOnGlobalLayoutListener(this)
        isListeningToGlobalLayout = false
    }

    fun dispose() {
        unregisterOnGlobalLayoutListener()
        view.removeOnAttachStateChangeListener(this)
    }
}

/**
 * Scope for [ExposedDropdownMenuBox].
 */
interface ExposedDropdownMenuBoxScope {
    /**
     * Modifier which should be applied to an [ExposedDropdownMenu]
     * placed inside the scope. It's responsible for
     * setting the width of the [ExposedDropdownMenu], which
     * will match the width of the [TextField]
     * (if [matchTextFieldWidth] is set to true).
     * Also it'll change the height of [ExposedDropdownMenu], so
     * it'll take the largest possible height to not overlap
     * the [TextField] and the software keyboard.
     *
     * @param matchTextFieldWidth Whether menu should match
     * the width of the text field to which it's attached.
     * If set to true the width will match the width
     * of the text field.
     */
    fun Modifier.exposedDropdownSize(
        matchTextFieldWidth: Boolean = true
    ): Modifier

    /**
     * Popup which contains content for Exposed Dropdown Menu.
     * Should be used inside the content of [ExposedDropdownMenuBox].
     *
     * @param expanded Whether the menu is currently open and visible to the user
     * @param onDismissRequest Called when the user requests to dismiss the menu, such as by
     * tapping outside the menu's bounds
     * @param modifier The modifier to apply to this layout
     * @param content The content of the [ExposedDropdownMenu]
     */
    @Composable
    fun ExposedDropdownMenu(
        expanded: Boolean,
        onDismissRequest: () -> Unit,
        modifier: Modifier = Modifier,
        content: @Composable ColumnScope.() -> Unit
    ) {
        // TODO(b/202810604): use DropdownMenu when PopupProperties constructor is stable
        // return DropdownMenu(
        //     expanded = expanded,
        //     onDismissRequest = onDismissRequest,
        //     modifier = modifier.exposedDropdownSize(),
        //     properties = ExposedDropdownMenuDefaults.PopupProperties,
        //     content = content
        // )

        val expandedStates = remember { MutableTransitionState(false) }
        expandedStates.targetState = expanded

        if (expandedStates.currentState || expandedStates.targetState) {
            val transformOriginState = remember { mutableStateOf(TransformOrigin.Center) }
            val density = LocalDensity.current
            val popupPositionProvider = DropdownMenuPositionProvider(
                DpOffset.Zero,
                density
            ) { parentBounds, menuBounds ->
                transformOriginState.value = calculateTransformOrigin(parentBounds, menuBounds)
            }

            ExposedDropdownMenuPopup(
                onDismissRequest = onDismissRequest,
                popupPositionProvider = popupPositionProvider
            ) {
                DropdownMenuContent(
                    expandedStates = expandedStates,
                    transformOriginState = transformOriginState,
                    modifier = modifier.exposedDropdownSize(),
                    content = content
                )
            }
        }
    }
}

/**
 * Contains default values used by Exposed Dropdown Menu.
 */
object ExposedDropdownMenuDefaults {
    /**
     * Default trailing icon for Exposed Dropdown Menu.
     *
     * @param expanded Whether [ExposedDropdownMenuBoxScope.ExposedDropdownMenu]
     * is expanded or not. Affects the appearance of the icon.
     * @param onIconClick Called when the icon was clicked.
     */
    @Composable
    fun TrailingIcon(
        expanded: Boolean,
        onIconClick: () -> Unit = {}
    ) {
        // Clear semantics here as otherwise icon will be a11y focusable but without an
        // action. When there's an API to check if Talkback is on, developer will be able to
        // expand the menu on icon click in a11y mode only esp. if using their own custom
        // trailing icon.
        IconButton(onClick = onIconClick, modifier = Modifier.clearAndSetSemantics { }) {
            Icon(
                Icons.Filled.ArrowDropDown,
                "Trailing icon for exposed dropdown menu",
                Modifier.rotate(
                    if (expanded)
                        180f
                    else
                        360f
                )
            )
        }
    }

    /**
     * Creates a [TextFieldColors] that represents the default input text, background and content
     * (including label, placeholder, leading and trailing icons) colors used in a [TextField].
     *
     * @param textColor Represents the color used for the input text of this text field.
     * @param disabledTextColor Represents the color used for the input text of this text field
     * when it's disabled.
     * @param backgroundColor Represents the background color for this text field.
     * @param cursorColor Represents the cursor color for this text field.
     * @param errorCursorColor Represents the cursor color for this text field
     * when it's in error state.
     * @param focusedIndicatorColor Represents the indicator color for this text field
     * when it's focused.
     * @param unfocusedIndicatorColor Represents the indicator color for this text field
     * when it's not focused.
     * @param disabledIndicatorColor Represents the indicator color for this text field
     * when it's disabled.
     * @param errorIndicatorColor Represents the indicator color for this text field
     * when it's in error state.
     * @param leadingIconColor Represents the leading icon color for this text field.
     * @param disabledLeadingIconColor Represents the leading icon color for this text field
     * when it's disabled.
     * @param errorLeadingIconColor Represents the leading icon color for this text field
     * when it's in error state.
     * @param trailingIconColor Represents the trailing icon color for this text field.
     * @param focusedTrailingIconColor Represents the trailing icon color for this text field
     * when it's focused.
     * @param disabledTrailingIconColor Represents the trailing icon color for this text field
     * when it's disabled.
     * @param errorTrailingIconColor Represents the trailing icon color for this text field
     * when it's in error state.
     * @param focusedLabelColor Represents the label color for this text field
     * when it's focused.
     * @param unfocusedLabelColor Represents the label color for this text field
     * when it's not focused.
     * @param disabledLabelColor Represents the label color for this text field
     * when it's disabled.
     * @param errorLabelColor Represents the label color for this text field
     * when it's in error state.
     * @param placeholderColor Represents the placeholder color for this text field.
     * @param disabledPlaceholderColor Represents the placeholder color for this text field
     * when it's disabled.
     */
    @Composable
    fun textFieldColors(
        textColor: Color = LocalContentColor.current.copy(LocalContentAlpha.current),
        disabledTextColor: Color = textColor.copy(ContentAlpha.disabled),
        backgroundColor: Color =
            MaterialTheme.colorScheme.onSurface.copy(alpha = TextFieldDefaults.BackgroundOpacity),
        cursorColor: Color = MaterialTheme.colorScheme.primary,
        errorCursorColor: Color = MaterialTheme.colorScheme.error,
        focusedIndicatorColor: Color =
            MaterialTheme.colorScheme.primary.copy(alpha = ContentAlpha.high),
        unfocusedIndicatorColor: Color =
            MaterialTheme.colorScheme.onSurface.copy(
                alpha = TextFieldDefaults.UnfocusedIndicatorLineOpacity
            ),
        disabledIndicatorColor: Color = unfocusedIndicatorColor.copy(alpha = ContentAlpha.disabled),
        errorIndicatorColor: Color = MaterialTheme.colorScheme.error,
        leadingIconColor: Color =
            MaterialTheme.colorScheme.onSurface.copy(alpha = TextFieldDefaults.IconOpacity),
        disabledLeadingIconColor: Color = leadingIconColor.copy(alpha = ContentAlpha.disabled),
        errorLeadingIconColor: Color = leadingIconColor,
        trailingIconColor: Color =
            MaterialTheme.colorScheme.onSurface.copy(alpha = TextFieldDefaults.IconOpacity),
        disabledTrailingIconColor: Color = trailingIconColor.copy(alpha = ContentAlpha.disabled),
        errorTrailingIconColor: Color = MaterialTheme.colorScheme.error,
        focusedLabelColor: Color =
            MaterialTheme.colorScheme.primary.copy(alpha = ContentAlpha.high),
        unfocusedLabelColor: Color = MaterialTheme.colorScheme.onSurface.copy(ContentAlpha.medium),
        disabledLabelColor: Color = unfocusedLabelColor.copy(ContentAlpha.disabled),
        errorLabelColor: Color = MaterialTheme.colorScheme.error,
        placeholderColor: Color = MaterialTheme.colorScheme.onSurface.copy(ContentAlpha.medium),
        disabledPlaceholderColor: Color = placeholderColor.copy(ContentAlpha.disabled)
    ) = com.fiqhsearcher.components.textfields.textFieldColors(
        textColor = textColor,
        disabledTextColor = disabledTextColor,
        cursorColor = cursorColor,
        errorCursorColor = errorCursorColor,
        focusedIndicatorColor = focusedIndicatorColor,
        unfocusedIndicatorColor = unfocusedIndicatorColor,
        errorIndicatorColor = errorIndicatorColor,
        disabledIndicatorColor = disabledIndicatorColor,
        leadingIconColor = leadingIconColor,
        disabledLeadingIconColor = disabledLeadingIconColor,
        errorLeadingIconColor = errorLeadingIconColor,
        trailingIconColor = trailingIconColor,
        disabledTrailingIconColor = disabledTrailingIconColor,
        errorTrailingIconColor = errorTrailingIconColor,
        backgroundColor = backgroundColor,
        focusedLabelColor = focusedLabelColor,
        unfocusedLabelColor = unfocusedLabelColor,
        disabledLabelColor = disabledLabelColor,
        errorLabelColor = errorLabelColor,
        placeholderColor = placeholderColor,
        disabledPlaceholderColor = disabledPlaceholderColor
    )
}

private fun Modifier.expandable(
    onExpandedChange: () -> Unit,
    menuLabel: String
) = pointerInput(Unit) {
    forEachGesture {
        coroutineScope {
            awaitPointerEventScope {
                var event: PointerEvent
                do {
                    event = awaitPointerEvent(PointerEventPass.Initial)
                } while (
                    !event.changes.all { it.changedToUp() }
                )
                onExpandedChange.invoke()
            }
        }
    }
}.semantics {
    contentDescription = menuLabel // this should be a localised string
    onClick {
        onExpandedChange()
        true
    }
}

private fun updateHeight(
    view: View,
    coordinates: LayoutCoordinates?,
    verticalMarginInPx: Int,
    onHeightUpdate: (Int) -> Unit
) {
    coordinates ?: return
    val visibleWindowBounds = Rect().let {
        view.getWindowVisibleDisplayFrame(it)
        it
    }
    val heightAbove = coordinates.boundsInWindow().top - visibleWindowBounds.top
    val heightBelow =
        visibleWindowBounds.bottom - visibleWindowBounds.top - coordinates.boundsInWindow().bottom
    onHeightUpdate(max(heightAbove, heightBelow).toInt() - verticalMarginInPx)
}
