package com.copperleaf.scripturenow.db.verses.sql

import com.benasher44.uuid.Uuid
import com.copperleaf.scripturenow.Sn_memoryVerseQueries
import com.copperleaf.scripturenow.db.verses.MemoryVersesDb
import com.copperleaf.scripturenow.repositories.verses.models.MemoryVerse
import com.copperleaf.scripturenow.utils.mapEach
import com.copperleaf.scripturenow.utils.mapIfNotNull
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow

class MemoryVersesSqlDb(
    private val queries: Sn_memoryVerseQueries,
    private val converter: MemoryVersesSqlDbConverter,
) : MemoryVersesDb {
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
}
