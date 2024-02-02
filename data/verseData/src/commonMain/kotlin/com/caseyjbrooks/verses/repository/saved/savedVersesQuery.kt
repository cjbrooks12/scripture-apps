package com.caseyjbrooks.verses.repository.saved

import com.caseyjbrooks.verses.models.ArchiveStatus
import com.caseyjbrooks.verses.models.SavedVerse
import com.caseyjbrooks.verses.models.VerseTag

internal fun List<SavedVerse>.filterByArchiveStatus(archiveStatus: ArchiveStatus): List<SavedVerse> {
    return when (archiveStatus) {
        ArchiveStatus.NotArchived -> this.filter { !it.archived }
        ArchiveStatus.Archived -> this.filter { it.archived }
        ArchiveStatus.FullCollection -> this
    }
}

internal fun List<SavedVerse>.filterByTag(tags: Set<VerseTag>): List<SavedVerse> {
    return if (tags.isNotEmpty()) {
        this.filter { filteredVerse ->
            tags.all { tag ->
                filteredVerse.tags.contains(tag)
            }
        }
    } else {
        this
    }
}
