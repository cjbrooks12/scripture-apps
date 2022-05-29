package com.copperleaf.scripturenow.api.votd.ourmanna

import com.copperleaf.scripturenow.repositories.votd.models.VerseOfTheDay
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
