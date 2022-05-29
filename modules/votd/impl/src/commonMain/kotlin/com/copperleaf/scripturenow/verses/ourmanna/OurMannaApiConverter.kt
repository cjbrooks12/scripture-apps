package com.copperleaf.scripturenow.verses.ourmanna

import com.copperleaf.scripturenow.verses.VerseOfTheDay
import kotlinx.datetime.LocalDate


interface OurMannaApiConverter {
    fun convertApiModelToRepositoryModel(
        date: LocalDate,
        apiModel: OurMannaVotdResponse,
    ) : VerseOfTheDay
}
