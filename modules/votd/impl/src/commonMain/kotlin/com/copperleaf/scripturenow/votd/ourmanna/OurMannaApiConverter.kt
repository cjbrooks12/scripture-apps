package com.copperleaf.scripturenow.votd.ourmanna

import com.copperleaf.scripturenow.votd.VerseOfTheDay
import kotlinx.datetime.LocalDate


interface OurMannaApiConverter {
    fun convertApiModelToRepositoryModel(
        date: LocalDate,
        apiModel: OurMannaVotdResponse,
    ) : VerseOfTheDay
}
