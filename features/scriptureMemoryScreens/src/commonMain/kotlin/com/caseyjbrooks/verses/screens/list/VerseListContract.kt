package com.caseyjbrooks.verses.screens.list

import com.caseyjbrooks.verses.models.ArchiveStatus
import com.caseyjbrooks.verses.models.SavedVerse
import com.caseyjbrooks.verses.models.VerseTag
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.map

internal object VerseListContract {
    internal data class State(
        val cachedVerses: Cached<List<SavedVerse>> = Cached.NotLoaded(),
        val archiveStatus: ArchiveStatus = ArchiveStatus.NotArchived,
        val tagFilter: List<VerseTag> = emptyList(),
    ) {
        val allTags: Cached<List<VerseTag>> = cachedVerses.map { verses ->
            verses
                .flatMap { it.tags }
                .distinct()
                .sortedBy { it.tag }
        }
        val inactiveTags: Cached<List<VerseTag>> = allTags.map { tags ->
            (tags - tagFilter)
                .sortedBy { it.tag }
        }
    }

    internal sealed interface Inputs {
        data object ObserveVerseList : Inputs
        data class VersesUpdated(val cachedVerses: Cached<List<SavedVerse>>) : Inputs

        data class SetArchiveStatus(val archiveStatus: ArchiveStatus) : Inputs
        data class AddTagFilter(val tag: VerseTag) : Inputs
        data class RemoveTagFilter(val tag: VerseTag) : Inputs

        data object CreateNewVerse : Inputs
        data class ViewVerseDetails(val verse: SavedVerse) : Inputs
        data class EditVerse(val verse: SavedVerse) : Inputs
        data class Practice(val verse: SavedVerse) : Inputs
        data class Archive(val verse: SavedVerse) : Inputs
        data class RestoreFromArchive(val verse: SavedVerse) : Inputs

        data object GoBack : Inputs
    }

    internal sealed interface Events {
        data class NavigateTo(val destination: String, val replaceTop: Boolean = false) : Events
        data object NavigateBack : Events
    }
}
