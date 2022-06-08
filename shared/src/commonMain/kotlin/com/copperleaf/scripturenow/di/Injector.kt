package com.copperleaf.scripturenow.di

import co.touchlab.kermit.Logger
import com.copperleaf.scripturenow.repositories.router.MainRouterViewModel
import com.copperleaf.scripturenow.ui.verses.detail.MemoryVerseDetailsViewModel
import com.copperleaf.scripturenow.ui.verses.edit.CreateOrEditMemoryVerseViewModel
import com.copperleaf.scripturenow.ui.verses.list.MemoryVerseListViewModel
import com.copperleaf.scripturenow.ui.votd.VotdViewModel
import kotlinx.coroutines.CoroutineScope

interface Injector {
    val mainRouter: MainRouterViewModel
    fun logger(tag: String): Logger
    fun votdViewModel(coroutineScope: CoroutineScope): VotdViewModel
    fun verseListViewModel(coroutineScope: CoroutineScope): MemoryVerseListViewModel
    fun createOrEditVerseViewModel(coroutineScope: CoroutineScope): CreateOrEditMemoryVerseViewModel
    fun verseDetailsViewModel(coroutineScope: CoroutineScope): MemoryVerseDetailsViewModel
}
