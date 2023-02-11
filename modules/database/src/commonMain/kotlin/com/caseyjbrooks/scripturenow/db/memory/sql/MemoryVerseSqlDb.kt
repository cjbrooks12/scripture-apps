package com.caseyjbrooks.scripturenow.db.memory.sql

import com.benasher44.uuid.Uuid
import com.caseyjbrooks.scripturenow.db.memory.MemoryVerseDb
import com.caseyjbrooks.scripturenow.models.memory.MemoryVerse
import com.caseyjbrooks.scripturenow.utils.mapEach
import com.caseyjbrooks.scripturenow.utils.mapIfNotNull
import com.copperleaf.scripturenow.Sn_memoryVerseQueries
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow

internal class MemoryVerseSqlDb(
    private val queries: Sn_memoryVerseQueries,
    private val converter: MemoryVerseSqlDbConverter,
) : MemoryVerseDb {
    override fun getMemoryVerses(): Flow<List<MemoryVerse>> {
        return queries
            .getAll()
            .asFlow()
            .mapToList()
            .mapEach(converter::convertDbModelToRepositoryModel)
    }

    override fun getVerseById(id: Uuid): Flow<MemoryVerse?> {
        return queries
            .getById(id)
            .asFlow()
            .mapToOneOrNull()
            .mapIfNotNull(converter::convertDbModelToRepositoryModel)
    }

    override suspend fun saveVerse(verse: MemoryVerse) {
        queries
            .insertOrReplace(converter.convertRepositoryModelToDbModel(verse))
    }

    override suspend fun deleteVerse(verse: MemoryVerse) {
        queries
            .delete(verse.uuid)
    }
}
