package com.caseyjbrooks.app.widgets.votd

import com.copperleaf.scripturenow.di.kodein.ApplicationContext
import com.copperleaf.scripturenow.repositories.votd.VotdInterceptor
import org.kodein.di.DI
import org.kodein.di.inSet
import org.kodein.di.instance
import org.kodein.di.provider

fun votdWidgetModule(): DI.Module = DI.Module(name = "Widget >> Votd") {
    inSet<VotdInterceptor> {
        provider {
            VotdWidgetReceiver.Updater(instance(tag = ApplicationContext))
        }
    }
}
