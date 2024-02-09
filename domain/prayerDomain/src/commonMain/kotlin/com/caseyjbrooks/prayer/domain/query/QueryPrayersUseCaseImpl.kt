package com.caseyjbrooks.prayer.domain.query

import com.caseyjbrooks.prayer.models.ArchiveStatus
import com.caseyjbrooks.prayer.models.PrayerTag
import com.caseyjbrooks.prayer.models.SavedPrayer
import com.caseyjbrooks.prayer.models.SavedPrayerType
import com.caseyjbrooks.prayer.repository.saved.SavedPrayersRepository
import com.copperleaf.ballast.repository.cache.Cached
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEmpty

internal class QueryPrayersUseCaseImpl(
    private val repository: SavedPrayersRepository,
) : QueryPrayersUseCase {

    override operator fun invoke(
        archiveStatus: ArchiveStatus,
        prayerType: Set<SavedPrayerType>,
        tags: Set<PrayerTag>,
    ): Flow<Cached<List<SavedPrayer>>> {
        return flow {
            emit(Cached.Fetching(null))

            try {
                repository
                    .getPrayers(archiveStatus, prayerType, tags, null)
                    .map { Cached.Value(it) }
                    .onEmpty { Cached.Value(emptyList<SavedPrayer>()) }
                    .catch { Cached.FetchingFailed<List<SavedPrayer>>(it, null) }
                    .let { emitAll(it) }
            } catch (t: Throwable) {
                t.printStackTrace()
                throw t
            }
        }
    }
}
