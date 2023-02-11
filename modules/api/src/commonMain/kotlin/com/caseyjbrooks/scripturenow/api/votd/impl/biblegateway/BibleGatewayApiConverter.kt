package com.caseyjbrooks.scripturenow.api.votd.impl.biblegateway

import com.caseyjbrooks.scripturenow.api.votd.impl.biblegateway.models.BibleGatewayVotdResponse
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDay
import kotlinx.datetime.LocalDate

internal interface BibleGatewayApiConverter {
    public fun convertApiModelToRepositoryModel(
        date: LocalDate,
        apiModel: BibleGatewayVotdResponse,
    ): VerseOfTheDay
}
