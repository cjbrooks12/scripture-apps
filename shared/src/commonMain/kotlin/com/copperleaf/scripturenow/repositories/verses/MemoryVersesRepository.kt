package com.copperleaf.scripturenow.repositories.verses

import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.scripturenow.repositories.verses.models.MemoryVerse
import kotlinx.coroutines.flow.Flow

interface MemoryVersesRepository {

    fun getDataList(refreshCache: Boolean = false): Flow<Cached<List<MemoryVerse>>>

}
