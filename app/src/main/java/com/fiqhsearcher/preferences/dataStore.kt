package com.fiqhsearcher.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore(name = "fiqhSearcher")

val MADHAB = intPreferencesKey("madhab")
val AUTHENTICATION = stringPreferencesKey("authentication")
val DARK_THEME = booleanPreferencesKey("darkTheme")
