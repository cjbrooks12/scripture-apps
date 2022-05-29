package com.copperleaf.scripturenow.verses.models

class Verse(
    val id: Int = -1,
    val reference: String = "",
    val text: String = "",
    val status: VerseStatus = VerseStatus.New,
    val tags: List<VerseTag> = emptyList(),
)
