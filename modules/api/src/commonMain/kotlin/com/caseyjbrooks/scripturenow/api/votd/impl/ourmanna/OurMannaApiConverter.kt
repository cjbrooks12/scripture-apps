package com.caseyjbrooks.scripturenow.api.votd.impl.ourmanna

import com.caseyjbrooks.scripturenow.api.votd.impl.ourmanna.models.OurMannaVotdResponse
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDay
import kotlinx.datetime.LocalDate

internal interface OurMannaApiConverter {
    public fun convertApiModelToRepositoryModel(
        date: LocalDate,
        apiModel: OurMannaVotdResponse,
    ): VerseOfTheDay
}
