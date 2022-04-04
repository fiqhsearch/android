package com.fiqhsearcher.supabase

import android.content.Context
import io.supabase.common.SupportedStorage
import java.util.*
import javax.inject.Inject

class PropertiesStorage @Inject constructor(context: Context) : SupportedStorage {

    private val file = context.filesDir
        .resolve("authentication.properties")
        .also { it.createNewFile() }

    private val properties = Properties().also {
        file.reader().use { r -> it.load(r) }
    }

    override fun get(key: String): String? {
        return properties[key] as String?
    }

    override fun remove(key: String) {
        properties.remove(key)
    }

    override fun save() {
        file.writer().use { properties.store(it, null) }
    }

    override fun set(key: String, value: String) {
        properties[key] = value
    }
}