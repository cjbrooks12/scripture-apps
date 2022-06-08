package com.copperleaf.scripturenow.db.verses.sql

import com.copperleaf.scripturenow.Sn_memoryVerse
import com.copperleaf.scripturenow.common.now
import com.copperleaf.scripturenow.repositories.verses.models.MemoryVerse
import kotlinx.datetime.LocalDateTime

class MemoryVersesSqlDbConverterImpl : MemoryVersesSqlDbConverter {

    override fun convertDbModelToRepositoryModel(
        dbModel: Sn_memoryVerse
    ): MemoryVerse = with(dbModel) {
        MemoryVerse(
            uuid = dbModel.uuid,
            main = false,
            text = dbModel.text,
            reference = dbModel.reference,
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
            text = repositoryModel.text,
            reference = repositoryModel.reference,
            version = repositoryModel.version,
            verse_url = repositoryModel.verseUrl,
            notice = repositoryModel.notice,
            created_at = repositoryModel.createdAt,
            updated_at = LocalDateTime.now(),
        )
    }
}
