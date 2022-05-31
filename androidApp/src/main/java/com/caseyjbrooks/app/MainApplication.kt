package com.caseyjbrooks.app

import android.app.Application
import android.content.Context
import com.caseyjbrooks.app.widgets.votd.votdWidgetModule
import com.copperleaf.scripturenow.di.Injector
import com.copperleaf.scripturenow.di.kodein.ApplicationContext
import com.copperleaf.scripturenow.di.kodein.KodeinInjector
import org.kodein.di.bind
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
            }
        )
    }
}
