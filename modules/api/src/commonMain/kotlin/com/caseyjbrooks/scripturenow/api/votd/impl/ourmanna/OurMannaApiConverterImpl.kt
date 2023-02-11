package com.caseyjbrooks.scripturenow.api.votd.impl.ourmanna

import com.benasher44.uuid.uuid4
import com.caseyjbrooks.scripturenow.api.votd.impl.ourmanna.models.OurMannaVotdResponse
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDay
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDayService
import com.caseyjbrooks.scripturenow.utils.parseVerseReference
import kotlinx.datetime.LocalDate

internal class OurMannaApiConverterImpl : OurMannaApiConverter {
    override fun convertApiModelToRepositoryModel(
        date: LocalDate,
        apiModel: OurMannaVotdResponse,
    ): VerseOfTheDay = with(apiModel) {
        return VerseOfTheDay(
            uuid = uuid4(),
            providedBy = VerseOfTheDayService.OurManna,
            date = date,
            text = verse.details.text,
            reference = verse.details.reference.parseVerseReference(),
            version = verse.details.version,
            verseUrl = verse.details.verseurl,
            notice = verse.notice,
        )
    }
}
