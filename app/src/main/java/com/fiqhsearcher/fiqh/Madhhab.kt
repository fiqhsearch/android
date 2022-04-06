package com.fiqhsearcher.fiqh

import androidx.annotation.StringRes
import com.fiqhsearcher.R.string.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter

enum class Madhhab(
    @StringRes val nameResource: Int
) {
    HANBALI(hanbali),
    SHAFII(shaifii),
    MALIKI(maliki),
    HANAFI(hanafi);

    val dataName = name.lowercase().replace("ii", "i'i")

    companion object {
        val values = values().toList()
        val dataToMap = values.associateBy { it.dataName }
    }
}

object MadhabAdapter : JsonAdapter<Madhhab>() {
    override fun fromJson(reader: JsonReader): Madhhab? {
        return Madhhab.dataToMap[reader.nextString()]
    }

    override fun toJson(writer: JsonWriter, value: Madhhab?) {
        if (value == null) writer.nullValue() else writer.value(value.dataName)
    }

}
