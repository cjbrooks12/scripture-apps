package com.caseyjbrooks.scripturenow.api.votd.impl.theysaidso

import com.caseyjbrooks.scripturenow.api.votd.impl.theysaidso.models.TheySaidSoBibleVerseResponse
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDay
import kotlinx.datetime.LocalDate

internal interface TheySaidSoApiConverter {
    public fun convertApiModelToRepositoryModel(
        date: LocalDate,
        apiModel: TheySaidSoBibleVerseResponse,
    ): VerseOfTheDay
}
