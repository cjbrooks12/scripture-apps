package com.copperleaf.scripturenow.ui.verses.edit

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

class CreateOrEditMemoryVerseEventHandler : EventHandler<
    CreateOrEditMemoryVerseContract.Inputs,
    CreateOrEditMemoryVerseContract.Events,
    CreateOrEditMemoryVerseContract.State> {
    override suspend fun EventHandlerScope<
        CreateOrEditMemoryVerseContract.Inputs,
        CreateOrEditMemoryVerseContract.Events,
        CreateOrEditMemoryVerseContract.State>.handleEvent(
        event: CreateOrEditMemoryVerseContract.Events
    ) = when (event) {
        is CreateOrEditMemoryVerseContract.Events.NavigateUp -> {

        }
    }
}
