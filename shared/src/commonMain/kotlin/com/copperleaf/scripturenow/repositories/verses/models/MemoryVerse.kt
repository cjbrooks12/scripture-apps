package com.copperleaf.scripturenow.repositories.verses.models

import com.benasher44.uuid.Uuid

data class MemoryVerse(
    val uuid: Uuid,
    val main: Boolean,
    val text: String,
    val reference: String,
    val version: String,
    val verseUrl: String,
    val notice: String,
)
