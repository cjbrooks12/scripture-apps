package com.caseyjbrooks.scripturenow.api.votd.impl.biblegateway

import com.benasher44.uuid.uuid4
import com.caseyjbrooks.scripturenow.api.votd.impl.biblegateway.models.BibleGatewayVotdResponse
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDay
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDayService
import com.caseyjbrooks.scripturenow.utils.parseVerseReference
import kotlinx.datetime.LocalDate

internal class BibleGatewayApiConverterImpl : BibleGatewayApiConverter {
    override fun convertApiModelToRepositoryModel(
        date: LocalDate,
        apiModel: BibleGatewayVotdResponse,
    ): VerseOfTheDay = with(apiModel) {
        return VerseOfTheDay(
            uuid = uuid4(),
            providedBy = VerseOfTheDayService.BibleGateway,
            date = date,
            text = channel.item.content.split("<br/><br/> ").first().trim(),
            reference = channel.item.title.parseVerseReference(),
            version = "NIV",
            verseUrl = channel.item.guid,
            notice = channel.item.rights,
        )
    }
}
