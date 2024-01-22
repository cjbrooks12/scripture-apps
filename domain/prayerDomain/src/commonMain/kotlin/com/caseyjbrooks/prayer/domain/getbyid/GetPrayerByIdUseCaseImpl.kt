package com.caseyjbrooks.prayer.domain.getbyid

import com.caseyjbrooks.prayer.models.PrayerId
import com.caseyjbrooks.prayer.models.SavedPrayer
import com.caseyjbrooks.prayer.repository.saved.SavedPrayersRepository
import com.copperleaf.ballast.repository.cache.Cached
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

internal class GetPrayerByIdUseCaseImpl(
    private val repository: SavedPrayersRepository,
) : GetPrayerByIdUseCase {
    override operator fun invoke(prayerId: PrayerId): Flow<Cached<SavedPrayer>> {
        return flow {
            emit(Cached.Fetching(null))

            repository
                .getPrayerById(prayerId)
                .map {
                    if (it != null) {
                        Cached.Value(it)
                    } else {
                        Cached.FetchingFailed<SavedPrayer>(NullPointerException(), null)
                    }
                }
                .catch { Cached.FetchingFailed<SavedPrayer>(it, null) }
                .let { emitAll(it) }
        }
    }
}
