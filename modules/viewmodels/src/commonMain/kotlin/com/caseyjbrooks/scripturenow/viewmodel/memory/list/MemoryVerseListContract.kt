package com.caseyjbrooks.scripturenow.viewmodel.memory.list

import com.caseyjbrooks.scripturenow.models.memory.MemoryVerse
import com.copperleaf.ballast.repository.cache.Cached

public object MemoryVerseListContract {
    public data class State(
        val verses: Cached<List<MemoryVerse>> = Cached.NotLoaded(),
    )

    public sealed class Inputs {
        public data class Initialize(val forceRefresh: Boolean) : Inputs()
        public data class VersesUpdated(val verses: Cached<List<MemoryVerse>>) : Inputs()
        public object CreateVerse : Inputs()
        public data class ViewVerse(val verse: MemoryVerse) : Inputs()
        public data class EditVerse(val verse: MemoryVerse) : Inputs()
        public data class DeleteVerse(val verse: MemoryVerse) : Inputs()
    }

    public sealed class Events {
        public data class NavigateTo(val destination: String) : Events()
        public object NavigateBack : Events()
    }
}
