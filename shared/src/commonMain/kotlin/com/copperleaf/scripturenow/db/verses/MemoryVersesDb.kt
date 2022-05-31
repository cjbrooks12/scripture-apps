package com.copperleaf.scripturenow.db.verses

import com.benasher44.uuid.Uuid
import com.copperleaf.scripturenow.repositories.verses.models.MemoryVerse
import kotlinx.coroutines.flow.Flow

interface MemoryVersesDb {
    fun getMemoryVerses(): Flow<List<MemoryVerse>>
    fun getVerseById(id: Uuid): Flow<MemoryVerse?>
    suspend fun saveVerse(verse: MemoryVerse)
}
