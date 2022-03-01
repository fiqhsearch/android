package com.fiqhsearcher.prefs

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiqhsearcher.Madhab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PrefsViewModel(context: Context) : ViewModel() {

    private val _darkTheme = MutableStateFlow(true)
    val darkTheme = _darkTheme.asStateFlow()

    private val _madhab = MutableStateFlow(Madhab.HANBALI)
    val madhab = _madhab.asStateFlow()

    init {
        viewModelScope.launch {
            context.dataStore.data.map { it[DARK_THEME] ?: true }.collect {
                _darkTheme.value = it
            }
        }
        viewModelScope.launch {
            context.dataStore.data.map { it[MADHAB] ?: 0 }.collect {
                _madhab.value = Madhab.values[it]
            }
        }
    }
}