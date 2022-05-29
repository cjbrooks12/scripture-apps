package com.copperleaf.scripturenow.api.votd.ourmanna

import com.copperleaf.scripturenow.repositories.votd.models.VerseOfTheDay
import kotlinx.datetime.LocalDate


interface OurMannaApiConverter {
    fun convertApiModelToRepositoryModel(
        date: LocalDate,
        apiModel: OurMannaVotdResponse,
    ) : VerseOfTheDay
}
