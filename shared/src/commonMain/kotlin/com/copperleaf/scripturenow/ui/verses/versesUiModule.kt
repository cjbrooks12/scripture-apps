package com.copperleaf.scripturenow.ui.verses

import com.copperleaf.scripturenow.ui.UiConfigBuilder
import com.copperleaf.scripturenow.ui.verses.detail.MemoryVerseDetailsEventHandler
import com.copperleaf.scripturenow.ui.verses.detail.MemoryVerseDetailsInputHandler
import com.copperleaf.scripturenow.ui.verses.detail.MemoryVerseDetailsViewModel
import com.copperleaf.scripturenow.ui.verses.edit.CreateOrEditMemoryVerseEventHandler
import com.copperleaf.scripturenow.ui.verses.edit.CreateOrEditMemoryVerseInputHandler
import com.copperleaf.scripturenow.ui.verses.edit.CreateOrEditMemoryVerseViewModel
import com.copperleaf.scripturenow.ui.verses.list.MemoryVerseListEventHandler
import com.copperleaf.scripturenow.ui.verses.list.MemoryVerseListInputHandler
import com.copperleaf.scripturenow.ui.verses.list.MemoryVerseListViewModel
import kotlinx.coroutines.CoroutineScope
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance

fun versesUiModule() = DI.Module(name = "UI > Verses") {
    bind<MemoryVerseListViewModel> {
        factory { arg: CoroutineScope ->
            MemoryVerseListViewModel(
                coroutineScope = arg,
                configBuilder = instance(tag = UiConfigBuilder),
                inputHandler = MemoryVerseListInputHandler(instance()),
                eventHandler = MemoryVerseListEventHandler(instance()),
            )
        }
    }
    bind<MemoryVerseDetailsViewModel> {
        factory { arg: CoroutineScope ->
            MemoryVerseDetailsViewModel(
                coroutineScope = arg,
                configBuilder = instance(tag = UiConfigBuilder),
                inputHandler = MemoryVerseDetailsInputHandler(instance()),
                eventHandler = MemoryVerseDetailsEventHandler(instance()),
            )
        }
    }
    bind<CreateOrEditMemoryVerseViewModel> {
        factory { arg: CoroutineScope ->
            CreateOrEditMemoryVerseViewModel(
                coroutineScope = arg,
                configBuilder = instance(tag = UiConfigBuilder),
                inputHandler = CreateOrEditMemoryVerseInputHandler(instance()),
                eventHandler = CreateOrEditMemoryVerseEventHandler(instance()),
            )
        }
    }
}
