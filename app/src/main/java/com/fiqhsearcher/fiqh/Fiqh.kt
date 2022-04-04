package com.fiqhsearcher.fiqh

import androidx.annotation.StringRes
import com.fiqhsearcher.R

enum class Fiqh(
    @StringRes val nameRes: Int
) {

    IBADAT(R.string.ibadat),
    MO3AMALAT(R.string.mo3amalat),
    JINAYAT(R.string.jinayat),
    OSRAH(R.string.osrah)

}