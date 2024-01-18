package com.caseyjbrooks.prayer.domain.getdaily

import com.caseyjbrooks.prayer.models.DailyPrayer
import com.caseyjbrooks.prayer.repository.daily.DailyPrayerRepository
import com.copperleaf.ballast.repository.cache.Cached
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

public class GetDailyPrayerUseCaseImpl(
    private val repository: DailyPrayerRepository,
) : GetDailyPrayerUseCase {
    override operator fun invoke(): Flow<Cached<DailyPrayer>> {
        return flow {
            emit(Cached.Fetching(null))

            repository
                .getTodaysDailyPrayer()
                .map {
                    if (it != null) {
                        Cached.Value(it)
                    } else {
                        Cached.FetchingFailed<DailyPrayer>(NullPointerException(), null)
                    }
                }
                .catch { Cached.FetchingFailed<DailyPrayer>(it, null) }
                .let { emitAll(it) }
        }
    }
}
