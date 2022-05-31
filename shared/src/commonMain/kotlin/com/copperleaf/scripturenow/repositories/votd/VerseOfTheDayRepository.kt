package com.copperleaf.scripturenow.repositories.votd

import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.scripturenow.repositories.votd.models.VerseOfTheDay
import kotlinx.coroutines.flow.Flow

interface VerseOfTheDayRepository {

    fun getVerseOfTheDay(refreshCache: Boolean = false): Flow<Cached<VerseOfTheDay>>

}
