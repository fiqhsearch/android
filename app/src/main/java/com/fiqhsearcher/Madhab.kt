package com.fiqhsearcher

import androidx.annotation.StringRes
import com.fiqhsearcher.R.string.*

enum class Madhab(
    @StringRes val nameResource: Int
) {
    HANBALI(hanbali),
    SHAFII(shaifii),
    HANAFI(hanafi),
    MALIKI(maliki);

    companion object {
        val values = values().toList()
    }
}