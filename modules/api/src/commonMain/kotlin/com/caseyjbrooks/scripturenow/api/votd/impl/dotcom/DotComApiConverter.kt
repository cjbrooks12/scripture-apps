package com.caseyjbrooks.scripturenow.api.votd.impl.dotcom

import com.caseyjbrooks.scripturenow.api.votd.impl.dotcom.models.DotComVotdResponse
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDay
import kotlinx.datetime.LocalDate

internal interface DotComApiConverter {
    public fun convertApiModelToRepositoryModel(
        date: LocalDate,
        apiModel: DotComVotdResponse,
    ): VerseOfTheDay
}
