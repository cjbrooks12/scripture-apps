package com.caseyjbrooks.scripturenow.utils

import com.caseyjbrooks.scripturenow.models.VerseReference

private val BIBLE_VERSE_REGEX = """(.+?)\s*?(\d+)\s*?:\s*?(\d+)(?:\s*?-\s*?(\d+))?""".toRegex()

public fun String.parseVerseReference(): VerseReference {
    return BIBLE_VERSE_REGEX
        .matchEntire(this.trim())
        ?.destructured
        ?.let { (book, chapter, start, maybeEnd) ->
            val chapterNumber = chapter.toInt()
            val startNumber = start.toInt()
            val endNumber = maybeEnd.toIntOrNull() ?: startNumber
            VerseReference.KnownReference(book, chapterNumber, startNumber..endNumber)
        }
        ?: VerseReference.UnknownReference(this)
}

public val VerseReference.referenceText: String
    get() {
        return when (this) {
            is VerseReference.KnownReference -> {
                "$book $chapter:${if (verses.size == 1) verses.first else "${verses.first}-${verses.last}"}"
            }

            is VerseReference.UnknownReference -> {
                text
            }
        }
    }

public fun VerseReference.Companion.of(
    book: String,
    chapter: Int,
    verse: Int,
): VerseReference {
    return VerseReference.KnownReference(
        book = book,
        chapter = chapter,
        verses = verse..verse
    )
}
