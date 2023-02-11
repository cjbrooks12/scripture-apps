package com.caseyjbrooks.scripturenow.api.votd.impl.theysaidso

import com.benasher44.uuid.uuid4
import com.benasher44.uuid.uuid5Of
import com.caseyjbrooks.scripturenow.api.votd.impl.theysaidso.models.TheySaidSoBibleVerseResponse
import com.caseyjbrooks.scripturenow.models.VerseReference
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDay
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDayService
import com.caseyjbrooks.scripturenow.utils.of
import kotlinx.datetime.LocalDate

internal class TheySaidSoApiConverterImpl : TheySaidSoApiConverter {
    override fun convertApiModelToRepositoryModel(
        date: LocalDate,
        apiModel: TheySaidSoBibleVerseResponse,
    ): VerseOfTheDay = with(apiModel) {
        return VerseOfTheDay(
            uuid = contents.uuid?.let { uuid5Of(uuid4(), it) } ?: uuid4(),
            providedBy = VerseOfTheDayService.TheySaidSo,
            date = date,
            text = contents.verse,
            reference = VerseReference.of(contents.book, contents.chapter, contents.number),
            version = "",
            verseUrl = "https://theysaidso.com",
            notice = "Powered by quotes from theysaidso.com",
        )
    }
}
