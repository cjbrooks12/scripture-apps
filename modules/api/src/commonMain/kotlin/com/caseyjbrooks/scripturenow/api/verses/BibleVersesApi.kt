package com.caseyjbrooks.scripturenow.api.verses

import com.caseyjbrooks.scripturenow.models.VerseReference
import com.caseyjbrooks.scripturenow.models.verses.Verse

public interface BibleVersesApi {
    public suspend fun getVerseText(reference: VerseReference): List<Verse>
}
