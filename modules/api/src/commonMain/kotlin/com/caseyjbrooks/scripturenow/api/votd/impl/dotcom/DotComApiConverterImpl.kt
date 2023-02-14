package com.caseyjbrooks.scripturenow.api.votd.impl.dotcom

import com.benasher44.uuid.uuid4
import com.caseyjbrooks.scripturenow.api.votd.impl.dotcom.models.DotComVotdResponse
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDay
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDayService
import com.caseyjbrooks.scripturenow.utils.converter.Converter
import com.caseyjbrooks.scripturenow.utils.parseVerseReference
import kotlinx.datetime.LocalDate

internal class DotComApiConverterImpl : Converter<Pair<LocalDate, DotComVotdResponse>, VerseOfTheDay> {
    override fun convertValue(from: Pair<LocalDate, DotComVotdResponse>): VerseOfTheDay {
        val (date, apiModel) = from
        return VerseOfTheDay(
            uuid = uuid4(),
            providedBy = VerseOfTheDayService.VerseOfTheDayDotCom,
            date = date,
            text = apiModel.ogDescription,
            reference = apiModel.reference.parseVerseReference(),
            version = "NIV",
            verseUrl = apiModel.ogUrl,
            notice = "Powered by verseoftheday.com",
        )
    }
}
