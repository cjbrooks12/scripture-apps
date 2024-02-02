package com.caseyjbrooks.verses.domain.query

import com.caseyjbrooks.verses.models.ArchiveStatus
import com.caseyjbrooks.verses.models.SavedVerse
import com.caseyjbrooks.verses.models.VerseTag
import com.caseyjbrooks.verses.repository.saved.SavedVersesRepository
import com.copperleaf.ballast.repository.cache.Cached
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

internal class QueryVersesUseCaseImpl(
    private val repository: SavedVersesRepository,
) : QueryVersesUseCase {

    override operator fun invoke(
        archiveStatus: ArchiveStatus,
        tags: Set<VerseTag>,
    ): Flow<Cached<List<SavedVerse>>> {
        return flow {
            emit(Cached.Fetching(null))

            repository
                .getVerses(archiveStatus, tags)
                .map { Cached.Value(it) }
                .catch { Cached.FetchingFailed<List<SavedVerse>>(it, null) }
                .let { emitAll(it) }
        }
    }
}
