package com.caseyjbrooks.scripturenow.repositories.votd

import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDay
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDayService
import com.copperleaf.ballast.repository.cache.Cached
import kotlinx.coroutines.flow.Flow

public interface VerseOfTheDayRepository {

    public fun changeVerseOfTheDayService(service: VerseOfTheDayService)
    public fun getCurrentVerseOfTheDay(refreshCache: Boolean = false): Flow<Cached<VerseOfTheDay>>
}
