package com.fiqhsearcher.data

import com.fiqhsearcher.fiqh.Madhhab
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Section(
    val id: Int? = null,
    val madhhab: Madhhab,
    val name: String
)

@JsonClass(generateAdapter = true)
data class MadhabResult(
    val name: String
)

@JsonClass(generateAdapter = true)
data class Issue(
    val id: Int? = null,
    val madhhab: Madhhab,
    val section: Int,
    val topic: Int,
    val question: String = "",
    val proof: String = "",
    val answer: String = ""
)

@JsonClass(generateAdapter = true)
data class Topic(
    val id: Int? = null,
    val name: String,
    val madhhab: Madhhab,
    val section: Int
)