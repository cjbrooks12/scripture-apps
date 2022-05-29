package com.copperleaf.scripturenow.verses.ourmanna

import com.copperleaf.scripturenow.verses.VerseOfTheDay
import kotlinx.datetime.LocalDate

class OurMannaApiConverterImpl : OurMannaApiConverter {
    override fun convertApiModelToRepositoryModel(
        date: LocalDate,
        apiModel: OurMannaVotdResponse,
    ): VerseOfTheDay = with(apiModel) {
        return VerseOfTheDay(
            date = date,
            text = verse.details.text,
            reference = verse.details.reference,
            version = verse.details.version,
            verseUrl = verse.details.verseurl,
            notice = verse.notice,
        )
    }
}
