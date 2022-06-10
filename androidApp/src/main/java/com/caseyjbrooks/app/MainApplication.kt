package com.caseyjbrooks.app

import android.app.Application
import android.content.Context
import androidx.compose.material.SnackbarHostState
import com.caseyjbrooks.app.service.votd.VotdPrefetchWorker
import com.caseyjbrooks.app.ui.settings.SettingsEventHandler
import com.caseyjbrooks.app.utils.lifecycleplugins.RouterScreenAnalyticsInterceptor
import com.caseyjbrooks.app.widgets.votd.votdWidgetModule
import com.copperleaf.ballast.EventHandler
import com.copperleaf.scripturenow.api.auth.androidAuthApiModule
import com.copperleaf.scripturenow.di.kodein.ApplicationContext
import com.copperleaf.scripturenow.di.kodein.KodeinInjector
import com.copperleaf.scripturenow.repositories.router.RouterInterceptor
import com.copperleaf.scripturenow.ui.settings.SettingsContract
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import org.kodein.di.bind
import org.kodein.di.inSet
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton

class MainApplication : Application() {
    lateinit var injector: KodeinInjector

    override fun onCreate() {
        super.onCreate()

        injector = KodeinInjector.create {
            bind<Context>(tag = ApplicationContext) {
                singleton { this@MainApplication.applicationContext }
            }
            import(votdWidgetModule())
            import(androidAuthApiModule())
            bind<SnackbarHostState>() {
                singleton { SnackbarHostState() }
            }
            bind<EventHandler<SettingsContract.Inputs, SettingsContract.Events, SettingsContract.State>>() {
                provider {
                    SettingsEventHandler(
                        instance(),
                        instance(),
                        instance(),
                    )
                }
            }

            inSet<RouterInterceptor> {
                provider {
                    RouterScreenAnalyticsInterceptor(Firebase.analytics)
                }
            }
        }

        VotdPrefetchWorker.scheduleWork(this)
    }
}
