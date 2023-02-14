package com.caseyjbrooks.scripturenow.api.votd.impl.theysaidso

import com.benasher44.uuid.uuid4
import com.benasher44.uuid.uuid5Of
import com.caseyjbrooks.scripturenow.api.votd.impl.theysaidso.models.TheySaidSoBibleVerseResponse
import com.caseyjbrooks.scripturenow.models.VerseReference
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDay
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDayService
import com.caseyjbrooks.scripturenow.utils.converter.Converter
import com.caseyjbrooks.scripturenow.utils.of
import kotlinx.datetime.LocalDate

internal class TheySaidSoApiConverterImpl : Converter<Pair<LocalDate, TheySaidSoBibleVerseResponse>, VerseOfTheDay> {
    override fun convertValue(from: Pair<LocalDate, TheySaidSoBibleVerseResponse>): VerseOfTheDay {
        val (date, apiModel) = from
        return VerseOfTheDay(
            uuid = apiModel.contents.uuid?.let { uuid5Of(uuid4(), it) } ?: uuid4(),
            providedBy = VerseOfTheDayService.TheySaidSo,
            date = date,
            text = apiModel.contents.verse,
            reference = VerseReference.of(apiModel.contents.book, apiModel.contents.chapter, apiModel.contents.number),
            version = "",
            verseUrl = "https://theysaidso.com",
            notice = "Powered by quotes from theysaidso.com",
        )
    }
}
