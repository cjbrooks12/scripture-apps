package com.copperleaf.scripturenow.ui.router

import com.copperleaf.ballast.InputFilter
import com.copperleaf.ballast.router.RouterContract

class MainRouterFilter : InputFilter<
    RouterContract.Inputs,
    RouterContract.Events,
    RouterContract.State> {

    override fun filterInput(
        state: RouterContract.State,
        input: RouterContract.Inputs,
    ): InputFilter.Result {
        TODO("Not yet implemented")
    }
}
