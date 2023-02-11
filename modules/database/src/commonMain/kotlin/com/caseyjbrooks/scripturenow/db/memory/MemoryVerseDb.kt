package com.caseyjbrooks.scripturenow.db.memory

import com.benasher44.uuid.Uuid
import com.caseyjbrooks.scripturenow.models.memory.MemoryVerse
import kotlinx.coroutines.flow.Flow

public interface MemoryVerseDb {
    public fun getMemoryVerses(): Flow<List<MemoryVerse>>
    public fun getVerseById(id: Uuid): Flow<MemoryVerse?>
    public suspend fun saveVerse(verse: MemoryVerse)
    public suspend fun deleteVerse(verse: MemoryVerse)
}
