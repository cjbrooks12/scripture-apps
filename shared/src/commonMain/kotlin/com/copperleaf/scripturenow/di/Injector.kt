package com.copperleaf.scripturenow.di

import co.touchlab.kermit.Logger
import com.copperleaf.scripturenow.ui.router.MainRouterViewModel
import com.copperleaf.scripturenow.ui.votd.VotdViewModel
import kotlinx.coroutines.CoroutineScope

interface Injector {
    val mainRouter: MainRouterViewModel
    fun logger(tag: String): Logger
    fun votdViewModel(coroutineScope: CoroutineScope): VotdViewModel
}
