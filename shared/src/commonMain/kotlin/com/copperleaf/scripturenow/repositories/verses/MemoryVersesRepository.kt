package com.copperleaf.scripturenow.repositories.verses

import com.copperleaf.ballast.repository.cache.Cached
import kotlinx.coroutines.flow.Flow

interface MemoryVersesRepository {

    fun clearAllCaches()
    fun getDataList(refreshCache: Boolean = false): Flow<Cached<List<String>>>

}
