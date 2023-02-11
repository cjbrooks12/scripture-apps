package com.caseyjbrooks.scripturenow.db.memory.sql

import com.caseyjbrooks.scripturenow.models.memory.MemoryVerse
import com.copperleaf.scripturenow.Sn_memoryVerse

internal interface MemoryVerseSqlDbConverter {

    fun convertDbModelToRepositoryModel(
        dbModel: Sn_memoryVerse
    ): MemoryVerse

    fun convertRepositoryModelToDbModel(
        repositoryModel: MemoryVerse
    ): Sn_memoryVerse
}
