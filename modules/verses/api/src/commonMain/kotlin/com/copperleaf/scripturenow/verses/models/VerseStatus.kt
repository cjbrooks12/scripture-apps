package com.copperleaf.scripturenow.verses.models

sealed class VerseStatus {
    /**
     * This verse has just been created, but has not yet been officially started as a memory verse.
     */
    object New : VerseStatus()

    /**
     * The user is committed to memorizing this verse. The value of progress is customized by the app user, as an
     * indication of how well they are along in memorizing it.
     */
    data class InProgress(val name: String, val progress: Int) : VerseStatus()

    /**
     * This verse is fully memorized, but it should be reviewed every once in a while to keep it memorized.
     */
    object Memorized : VerseStatus()

    /**
     * The user has sent this verse to the archive, either because it's fully memorized, or because they chose to
     * abandon it for now.
     */
    object Archived : VerseStatus()

    /**
     * The verse has been soft-deleted, but may be restored.
     */
    object Deleted : VerseStatus()
}
