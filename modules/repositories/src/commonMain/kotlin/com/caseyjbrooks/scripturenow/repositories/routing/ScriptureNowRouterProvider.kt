package com.caseyjbrooks.scripturenow.repositories.routing

public fun interface ScriptureNowRouterProvider {
    public fun getScriptureNowRouter(deepLinkUrl: String?): ScriptureNowRouter
}
