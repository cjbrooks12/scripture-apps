package com.caseyjbrooks.scripturenow.viewmodel.memory.detail

import com.benasher44.uuid.Uuid
import com.caseyjbrooks.scripturenow.models.memory.MemoryVerse
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.isLoading

public object MemoryVerseDetailsContract {
    public data class State(
        val memoryVerse: Cached<MemoryVerse> = Cached.NotLoaded(),
    ) {
        val loading: Boolean = memoryVerse.isLoading()
    }

    public sealed class Inputs {
        public data class Initialize(val verseUuid: Uuid) : Inputs()
        public data class MemoryVerseUpdated(val memoryVerse: Cached<MemoryVerse>) : Inputs()
        public object EditVerse : Inputs()
        public object DeleteVerse : Inputs()
        public object GoBack : Inputs()
    }

    public sealed class Events {
        public data class NavigateTo(val destination: String) : Events()
        public object NavigateBack : Events()
    }
}
