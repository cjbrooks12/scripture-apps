package com.copperleaf.scripturenow.votd

import com.copperleaf.ballast.repository.cache.Cached
import kotlinx.coroutines.flow.Flow

interface VerseOfTheDayRepository {

    fun clearAllCaches()
    fun getVerseOfTheDay(refreshCache: Boolean = false): Flow<Cached<VerseOfTheDay>>

}
