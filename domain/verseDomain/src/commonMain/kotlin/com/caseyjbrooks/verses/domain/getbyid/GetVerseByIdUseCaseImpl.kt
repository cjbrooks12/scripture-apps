package com.caseyjbrooks.verses.domain.getbyid

import com.caseyjbrooks.verses.models.VerseId
import com.caseyjbrooks.verses.models.SavedVerse
import com.caseyjbrooks.verses.repository.saved.SavedVersesRepository
import com.copperleaf.ballast.repository.cache.Cached
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

internal class GetVerseByIdUseCaseImpl(
    private val repository: SavedVersesRepository,
) : GetVerseByIdUseCase {
    override operator fun invoke(verseId: VerseId): Flow<Cached<SavedVerse>> {
        return flow {
            emit(Cached.Fetching(null))

            repository
                .getVerseById(verseId)
                .map {
                    if (it != null) {
                        Cached.Value(it)
                    } else {
                        Cached.FetchingFailed<SavedVerse>(NullPointerException(), null)
                    }
                }
                .catch { Cached.FetchingFailed<SavedVerse>(it, null) }
                .let { emitAll(it) }
        }
    }
}
