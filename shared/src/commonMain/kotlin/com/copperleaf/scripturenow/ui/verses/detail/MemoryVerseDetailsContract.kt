package com.copperleaf.scripturenow.ui.verses.detail

import com.benasher44.uuid.Uuid
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.scripturenow.repositories.verses.models.MemoryVerse

object MemoryVerseDetailsContract {
    data class State(
        val memoryVerse: Cached<MemoryVerse> = Cached.NotLoaded(),
    )

    sealed class Inputs {
        data class Initialize(val verseUuid: Uuid) : Inputs()
        data class MemoryVerseUpdated(val memoryVerse: Cached<MemoryVerse>) : Inputs()
        object EditVerse : Inputs()
        object DeleteVerse : Inputs()
        object GoBack : Inputs()
    }

    sealed class Events {
        object NavigateBack : Events()
        data class NavigateTo(val destination: String) : Events()
    }
}
