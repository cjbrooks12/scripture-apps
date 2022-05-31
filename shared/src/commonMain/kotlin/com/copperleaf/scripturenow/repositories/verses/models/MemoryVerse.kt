package com.copperleaf.scripturenow.repositories.verses.models

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4

data class MemoryVerse(
    val uuid: Uuid = uuid4(),
    val text: String = "",
    val reference: String = "",
    val version: String = "",
    val verseUrl: String = "",
    val notice: String = "",
)
