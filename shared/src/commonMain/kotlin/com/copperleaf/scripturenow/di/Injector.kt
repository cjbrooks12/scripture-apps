package com.copperleaf.scripturenow.di

import com.copperleaf.scripturenow.ui.router.MainRouterViewModel
import com.copperleaf.scripturenow.ui.votd.VotdViewModel
import kotlinx.coroutines.CoroutineScope

interface Injector {
    val mainRouter: MainRouterViewModel
    fun votdViewModel(coroutineScope: CoroutineScope): VotdViewModel
}
