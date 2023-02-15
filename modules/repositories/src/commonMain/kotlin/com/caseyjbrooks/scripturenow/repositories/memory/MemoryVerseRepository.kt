package com.caseyjbrooks.scripturenow.repositories.memory

import com.benasher44.uuid.Uuid
import com.caseyjbrooks.scripturenow.models.VerseReference
import com.caseyjbrooks.scripturenow.models.memory.MemoryVerse
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDay
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.forms.core.ui.UiSchema
import com.copperleaf.json.schema.JsonSchema
import kotlinx.coroutines.flow.Flow

public interface MemoryVerseRepository {

    public fun getAllVerses(refreshCache: Boolean = false): Flow<Cached<List<MemoryVerse>>>
    public fun getVerseById(uuid: Uuid, refreshCache: Boolean = false): Flow<Cached<MemoryVerse>>
    public fun getVerseByReference(reference: VerseReference, refreshCache: Boolean = false): Flow<Cached<MemoryVerse>>
    public fun loadForm(): Flow<Cached<Pair<JsonSchema, UiSchema>>>

    public suspend fun createOrUpdateVerse(verse: MemoryVerse)
    public suspend fun saveAsMemoryVerse(verse: VerseOfTheDay)
    public suspend fun setAsMainMemoryVerse(verse: MemoryVerse)
    public suspend fun deleteVerse(verse: MemoryVerse)
}
