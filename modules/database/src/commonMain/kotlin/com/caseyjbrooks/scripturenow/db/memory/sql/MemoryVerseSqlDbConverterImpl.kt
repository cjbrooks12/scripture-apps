package com.caseyjbrooks.scripturenow.db.memory.sql

import com.caseyjbrooks.scripturenow.models.memory.MemoryVerse
import com.caseyjbrooks.scripturenow.utils.now
import com.caseyjbrooks.scripturenow.utils.parseVerseReference
import com.caseyjbrooks.scripturenow.utils.referenceText
import com.copperleaf.scripturenow.Sn_memoryVerse
import kotlinx.datetime.LocalDateTime

internal class MemoryVerseSqlDbConverterImpl : MemoryVerseSqlDbConverter {

    override fun convertDbModelToRepositoryModel(
        dbModel: Sn_memoryVerse
    ): MemoryVerse = with(dbModel) {
        MemoryVerse(
            uuid = dbModel.uuid,
            main = dbModel.main,
            text = dbModel.text,
            reference = dbModel.reference.parseVerseReference(),
            version = dbModel.version,
            verseUrl = dbModel.verse_url,
            notice = dbModel.notice,
            createdAt = dbModel.created_at,
            updatedAt = dbModel.updated_at,
        )
    }

    override fun convertRepositoryModelToDbModel(
        repositoryModel: MemoryVerse
    ): Sn_memoryVerse = with(repositoryModel) {
        Sn_memoryVerse(
            uuid = repositoryModel.uuid,
            main = repositoryModel.main,
            text = repositoryModel.text,
            reference = repositoryModel.reference.referenceText,
            version = repositoryModel.version,
            verse_url = repositoryModel.verseUrl,
            notice = repositoryModel.notice,
            created_at = repositoryModel.createdAt,
            updated_at = LocalDateTime.now(),
        )
    }
}
