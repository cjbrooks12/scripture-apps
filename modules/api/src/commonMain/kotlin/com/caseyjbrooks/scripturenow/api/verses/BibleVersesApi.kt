package com.caseyjbrooks.scripturenow.api.verses

import com.caseyjbrooks.scripturenow.models.verses.Bible
import com.caseyjbrooks.scripturenow.models.verses.Book
import com.caseyjbrooks.scripturenow.models.verses.Chapter
import com.caseyjbrooks.scripturenow.models.verses.Verse

public interface BibleVersesApi {
    public suspend fun getBibles(): List<Bible>
    public suspend fun getBooks(bible: Bible): List<Book>
    public suspend fun getChapters(bible: Bible, book: Book): List<Chapter>
    public suspend fun getVerses(bible: Bible, book: Book, chapter: Chapter): List<Verse>
}
