package com.caseyjbrooks.votd.domain.gettoday

import com.caseyjbrooks.votd.models.VerseOfTheDay
import com.caseyjbrooks.votd.repository.VerseOfTheDayRepository
import com.copperleaf.ballast.repository.cache.Cached
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

public class GetTodaysVerseOfTheDayUseCaseImpl(
    private val repository: VerseOfTheDayRepository
) : GetTodaysVerseOfTheDayUseCase {
    override operator fun invoke(): Flow<Cached<VerseOfTheDay>> {
        return flow {
            emit(Cached.Fetching(null))

            repository
                .getTodaysVerseOfTheDay()
                .map {
                    if (it != null) {
                        Cached.Value(it)
                    } else {
                        Cached.FetchingFailed<VerseOfTheDay>(NullPointerException(), null)
                    }
                }
                .catch { Cached.FetchingFailed<VerseOfTheDay>(it, null) }
                .let { emitAll(it) }
        }
    }
}
