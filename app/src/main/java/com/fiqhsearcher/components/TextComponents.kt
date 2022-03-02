package com.fiqhsearcher.components

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

val Context.activity: Activity?
    get() = when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.activity
        else -> null
    }