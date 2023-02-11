package com.caseyjbrooks.scripturenow.api.votd.impl.dotcom

import com.benasher44.uuid.uuid4
import com.caseyjbrooks.scripturenow.api.votd.impl.dotcom.models.DotComVotdResponse
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDay
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDayService
import com.caseyjbrooks.scripturenow.utils.parseVerseReference
import kotlinx.datetime.LocalDate

internal class DotComApiConverterImpl : DotComApiConverter {
    override fun convertApiModelToRepositoryModel(
        date: LocalDate,
        apiModel: DotComVotdResponse,
    ): VerseOfTheDay = with(apiModel) {
        return VerseOfTheDay(
            uuid = uuid4(),
            providedBy = VerseOfTheDayService.VerseOfTheDayDotCom,
            date = date,
            text = ogDescription,
            reference = reference.parseVerseReference(),
            version = "NIV",
            verseUrl = ogUrl,
            notice = "Powered by verseoftheday.com",
        )
    }
}
