package com.copperleaf.scripturenow.db.verses.sql

import com.copperleaf.scripturenow.Sn_memoryVerse
import com.copperleaf.scripturenow.repositories.verses.models.MemoryVerse

class MemoryVersesSqlDbConverterImpl : MemoryVersesSqlDbConverter {

    override fun convertDbModelToRepositoryModel(
        dbModel: Sn_memoryVerse
    ): MemoryVerse = with(dbModel) {
        TODO()
    }

    override fun convertRepositoryModelToDbModel(
        repositoryModel: MemoryVerse
    ): Sn_memoryVerse = with(repositoryModel) {
        TODO()
    }
}
