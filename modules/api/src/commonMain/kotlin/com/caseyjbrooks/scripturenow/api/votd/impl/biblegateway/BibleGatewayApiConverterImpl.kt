package com.caseyjbrooks.scripturenow.api.votd.impl.biblegateway

import com.benasher44.uuid.uuid4
import com.caseyjbrooks.scripturenow.api.votd.impl.biblegateway.models.BibleGatewayVotdResponse
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDay
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDayService
import com.caseyjbrooks.scripturenow.utils.converter.Converter
import com.caseyjbrooks.scripturenow.utils.parseVerseReference
import kotlinx.datetime.LocalDate

internal class BibleGatewayApiConverterImpl : Converter<Pair<LocalDate, BibleGatewayVotdResponse>, VerseOfTheDay> {
    override fun convertValue(from: Pair<LocalDate, BibleGatewayVotdResponse>): VerseOfTheDay {
        val (date, apiModel) = from
        return VerseOfTheDay(
            uuid = uuid4(),
            providedBy = VerseOfTheDayService.BibleGateway,
            date = date,
            text = apiModel.channel.item.content.split("<br/><br/> ").first().trim(),
            reference = apiModel.channel.item.title.parseVerseReference(),
            version = "NIV",
            verseUrl = apiModel.channel.item.guid,
            notice = apiModel.channel.item.rights,
        )
    }
}
