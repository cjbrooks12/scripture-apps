package com.caseyjbrooks.app

import android.app.Application
import android.content.Context
import com.caseyjbrooks.app.service.votd.VotdPrefetchWorker
import com.caseyjbrooks.app.utils.lifecycleplugins.RouterScreenAnalyticsInterceptor
import com.caseyjbrooks.app.widgets.votd.votdWidgetModule
import com.copperleaf.scripturenow.di.Injector
import com.copperleaf.scripturenow.di.kodein.ApplicationContext
import com.copperleaf.scripturenow.di.kodein.KodeinInjector
import com.copperleaf.scripturenow.repositories.router.RouterInterceptor
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import org.kodein.di.bind
import org.kodein.di.inSet
import org.kodein.di.provider
import org.kodein.di.singleton

class MainApplication : Application() {
    lateinit var injector: Injector

    override fun onCreate() {
        super.onCreate()

        injector = KodeinInjector.create(
            onBackstackEmptied = {

            },
            additionalConfig = {
                bind<Context>(tag = ApplicationContext) {
                    singleton { this@MainApplication.applicationContext }
                }
                import(votdWidgetModule())
                inSet<RouterInterceptor> {
                    provider {
                        RouterScreenAnalyticsInterceptor(Firebase.analytics)
                    }
                }
            }
        )

        VotdPrefetchWorker.scheduleWork(this)
    }
}
