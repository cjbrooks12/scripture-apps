package com.copperleaf.scripturenow.db.verses.sql

import com.copperleaf.scripturenow.Sn_memoryVerse
import com.copperleaf.scripturenow.repositories.verses.models.MemoryVerse

interface MemoryVersesSqlDbConverter {

    fun convertDbModelToRepositoryModel(
        dbModel: Sn_memoryVerse
    ) : MemoryVerse

    fun convertRepositoryModelToDbModel(
        repositoryModel: MemoryVerse
    ) : Sn_memoryVerse
}
