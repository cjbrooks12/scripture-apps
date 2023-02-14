package com.caseyjbrooks.scripturenow.api.votd.impl.ourmanna

import com.benasher44.uuid.uuid4
import com.caseyjbrooks.scripturenow.api.votd.impl.ourmanna.models.OurMannaVotdResponse
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDay
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDayService
import com.caseyjbrooks.scripturenow.utils.converter.Converter
import com.caseyjbrooks.scripturenow.utils.parseVerseReference
import kotlinx.datetime.LocalDate

internal class OurMannaApiConverterImpl : Converter<Pair<LocalDate, OurMannaVotdResponse>, VerseOfTheDay> {
    override fun convertValue(from: Pair<LocalDate, OurMannaVotdResponse>): VerseOfTheDay {
        val (date, apiModel) = from
        return VerseOfTheDay(
            uuid = uuid4(),
            providedBy = VerseOfTheDayService.OurManna,
            date = date,
            text = apiModel.verse.details.text,
            reference = apiModel.verse.details.reference.parseVerseReference(),
            version = apiModel.verse.details.version,
            verseUrl = apiModel.verse.details.verseurl,
            notice = apiModel.verse.notice,
        )
    }
}
