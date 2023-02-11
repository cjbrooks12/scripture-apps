package com.caseyjbrooks.scripturenow.api.search

import com.caseyjbrooks.scripturenow.models.verses.Verse

public interface BibleSearchApi {
    public suspend fun getSuggestedTopics(): List<String>
    public suspend fun search(query: String): List<Verse>
}
