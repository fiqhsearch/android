@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")

package com.fiqhsearcher.screen.browse.add

import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fiqhsearcher.R
import com.fiqhsearcher.components.PageTitle
import com.fiqhsearcher.components.dropdown.ExposedDropdownMenuBox
import com.fiqhsearcher.components.dropdown.ExposedDropdownMenuDefaults
import com.fiqhsearcher.components.textfields.TextField
import com.fiqhsearcher.fiqh.Madhhab
import com.fiqhsearcher.preferences.PreferencesViewModel
import com.fiqhsearcher.screen.browse.Searching
import com.fiqhsearcher.screen.search.SupabaseViewModel
import kotlinx.coroutines.launch

@Composable
fun NewSection(
    navigator: NavController,
    pref: PreferencesViewModel = hiltViewModel(),
    supabase: SupabaseViewModel = hiltViewModel()
) {
    val madhab by pref.madhab.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    val madhhabs = Madhhab.values
    var selected by remember { mutableStateOf(madhab) }
    var name by remember { mutableStateOf("") }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    ) {
        PageTitle(
            text = "إضافة قسم فقهي",
            padding = 20.dp
        )
        TextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            textStyle = LocalTextStyle.current.copy(
                textDirection = TextDirection.Rtl,
                fontFamily = FontFamily.Default
            ),
            label = {
                Text(
                    text = "اسم القسم",
                    modifier = Modifier.fillMaxWidth(),
                    style = LocalTextStyle.current.copy(
                        textDirection = TextDirection.Rtl,
                        fontFamily = FontFamily.Default
                    ),
                )
            }
        )
        val context = LocalContext.current
        DropMenu(
            expanded = expanded,
            dismiss = { expanded = !expanded },
            getName = { context.getString(it.nameResource) },
            list = madhhabs,
            label = "المذهب",
            selected = selected,
            onSelect = { selected = it }
        )
        val scope = rememberCoroutineScope()
        AddButton(
            text = "إضافة القسم",
            enabled = name.isNotEmpty(),
            onClick = {
                supabase.createSection(
                    madhhab = madhab,
                    name = name
                ).invokeOnCompletion {
                    scope.launch {
                        Toast
                            .makeText(context, "تمت إضافة القسم", Toast.LENGTH_LONG)
                            .show()
                        navigator.navigateUp()
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NewTopic(
    navigator: NavController,
    section: Int,
    pref: PreferencesViewModel = hiltViewModel(),
    supabase: SupabaseViewModel = hiltViewModel()
) {
    val madhab by pref.madhab.collectAsState()
    val list by supabase.getSections(madhab).collectAsState(initial = null)
    if (list == null) {
        Searching()
    } else {
        var expanded by remember { mutableStateOf(false) }
        var selected by remember { mutableStateOf(list!!.first { it.id == section }) }
        var name by remember { mutableStateOf("") }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            PageTitle(
                text = "إضافة باب فقهي",
                padding = 20.dp
            )
            TextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(
                    textDirection = TextDirection.Rtl,
                    fontFamily = FontFamily.Default
                ),
                label = {
                    Text(
                        text = stringResource(R.string.section_name),
                        modifier = Modifier.fillMaxWidth(),
                        style = LocalTextStyle.current.copy(
                            textDirection = TextDirection.Rtl,
                            fontFamily = FontFamily.Default
                        ),
                    )
                }
            )
            DropMenu(
                expanded = expanded,
                dismiss = { expanded = !expanded },
                getName = { it.name },
                list = list!!,
                label = stringResource(id = R.string.choose_section),
                selected = selected,
                onSelect = { selected = it }
            )
            val scope = rememberCoroutineScope()
            val context = LocalContext.current
            AddButton(
                text = "إضافة الباب",
                enabled = name.isNotEmpty(),
                onClick = {
                    supabase.createTopic(
                        section = selected.id!!,
                        madhhab = madhab,
                        name = name
                    ).invokeOnCompletion {
                        scope.launch {
                            Toast
                                .makeText(context, "تمت إضافة الباب", Toast.LENGTH_LONG)
                                .show()
                            navigator.navigateUp()
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun AddButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth(),
        enabled = enabled
    ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            fontSize = 17.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun <T> DropMenu(
    expanded: Boolean,
    dismiss: () -> Unit,
    getName: (T) -> String,
    list: List<T>,
    label: String,
    selected: T,
    onSelect: (T) -> Unit
) {
    ExposedDropdownMenuBox(
        modifier = Modifier.padding(10.dp),
        expanded = expanded,
        onExpandedChange = { dismiss() }
    ) {
        TextField(
            readOnly = true,
            value = getName(list.first { it == selected }),
            onValueChange = { },
            trailingIcon = {
                ToggleIcon(
                    expanded = expanded,
                    onIconClick = { dismiss() }
                )
            },
            textStyle = LocalTextStyle.current.copy(
                textDirection = TextDirection.Rtl,
                fontFamily = FontFamily.Default
            ),
            label = {
                Text(
                    text = label,
                    modifier = Modifier
                        .padding(3.dp)
                        .fillMaxWidth(),
                    style = LocalTextStyle.current.copy(
                        textDirection = TextDirection.Rtl,
                        fontFamily = FontFamily.Default
                    ),
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { dismiss() }
                ),
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            shape = RectangleShape,
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { dismiss() },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            list.forEach {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = getName(it),
                            fontSize = 17.sp,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    onClick = {
                        onSelect(it)
                        dismiss()
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun NewIssue(
    navigator: NavController,
    sectionId: Int,
    topicId: Int,
    pref: PreferencesViewModel = hiltViewModel(),
    supabase: SupabaseViewModel = hiltViewModel()
) {
    val madhab by pref.madhab.collectAsState()
    val sections by supabase.getSections(madhab).collectAsState(initial = null)
    val topics by supabase.getTopics(madhab, sectionId).collectAsState(initial = null)
    var expandedM by remember { mutableStateOf(false) }
    var expandedS by remember { mutableStateOf(false) }
    var expandedT by remember { mutableStateOf(false) }
    var question by remember { mutableStateOf("") }
    var answer by remember { mutableStateOf("") }
    var proof by remember { mutableStateOf("") }
    if (topics == null || sections == null) {
        Searching()
        return
    }
    var selectedM by remember { mutableStateOf(madhab) }
    var selectedS by remember { mutableStateOf(sections!!.first { it.id == sectionId }) }
    var selectedT by remember { mutableStateOf(topics!!.first { it.id == topicId }) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        PageTitle(
            text = "إضافة مسألة",
            padding = 20.dp
        )
        val context = LocalContext.current
        DropMenu(
            expanded = expandedM,
            dismiss = { expandedM = !expandedM },
            getName = { context.getString(it.nameResource) },
            list = Madhhab.values,
            label = "المذهب",
            selected = selectedM,
            onSelect = { selectedM = it }
        )
        DropMenu(
            expanded = expandedS,
            dismiss = { expandedS = !expandedS },
            getName = { it.name },
            list = sections!!,
            label = "القسم",
            selected = selectedS,
            onSelect = { selectedS = it }
        )
        DropMenu(
            expanded = expandedT,
            dismiss = { expandedT = !expandedT },
            getName = { it.name },
            list = topics!!,
            label = "الباب",
            selected = selectedT,
            onSelect = { selectedT = it }
        )
        TextField(
            value = question,
            onValueChange = { question = it },
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            textStyle = LocalTextStyle.current.copy(
                textDirection = TextDirection.Rtl,
                fontFamily = FontFamily.Default
            ),
            label = {
                Text(
                    text = "السؤال",
                    modifier = Modifier.fillMaxWidth(),
                    style = LocalTextStyle.current.copy(
                        textDirection = TextDirection.Rtl,
                        fontFamily = FontFamily.Default
                    )
                )
            }
        )
        TextField(
            value = answer,
            onValueChange = { answer = it },
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .width(IntrinsicSize.Min),
            textStyle = LocalTextStyle.current.copy(
                textDirection = TextDirection.Rtl,
                fontFamily = FontFamily.Default
            ),
            singleLine = false,
            label = {
                Text(
                    text = "الجواب",
                    modifier = Modifier.fillMaxWidth(),
                    style = LocalTextStyle.current.copy(
                        textDirection = TextDirection.Rtl,
                        fontFamily = FontFamily.Default
                    )
                )
            }
        )
        TextField(
            value = proof,
            onValueChange = { proof = it },
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .width(IntrinsicSize.Min),
            textStyle = LocalTextStyle.current.copy(
                textDirection = TextDirection.Rtl,
                fontFamily = FontFamily.Default
            ),
            singleLine = false,
            label = {
                Text(
                    text = "الدليل",
                    modifier = Modifier.fillMaxWidth(),
                    style = LocalTextStyle.current.copy(
                        textDirection = TextDirection.Rtl,
                        fontFamily = FontFamily.Default
                    )
                )
            }
        )

        val scope = rememberCoroutineScope()
        AddButton(
            text = "إضافة المسألة",
            enabled = question.isNotEmpty() && answer.isNotEmpty() && proof.isNotEmpty(),
            onClick = {
                supabase.createIssue(
                    madhhab = selectedM,
                    answer = answer,
                    topic = selectedT.id!!,
                    section = selectedS.id!!,
                    proof = proof,
                    question = question
                ).invokeOnCompletion {
                    scope.launch {
                        Toast
                            .makeText(context, "تمت إضافة المسألة", Toast.LENGTH_LONG)
                            .show()
                        navigator.navigateUp()
                    }
                }
            }
        )
    }
}

@Composable
fun ToggleIcon(
    expanded: Boolean,
    onIconClick: () -> Unit = {}
) {
    // Clear semantics here as otherwise icon will be a11y focusable but without an
    // action. When there's an API to check if Talkback is on, developer will be able to
    // expand the menu on icon click in a11y mode only esp. if using their own custom
    // trailing icon.
    val rotation = remember { Animatable(360f) }
    LaunchedEffect(expanded) {
        if (expanded)
            rotation.animateTo(180f)
        else
            rotation.animateTo(360f)
    }
    IconButton(onClick = onIconClick, modifier = Modifier.clearAndSetSemantics { }) {
        Icon(
            Icons.Filled.ArrowDropDown,
            "Trailing icon for exposed dropdown menu",
            Modifier.rotate(rotation.value)
        )
    }
}