package com.caseyjbrooks.app.di

import com.caseyjbrooks.scripturenow.appwidgets.votd.VerseOfTheDayWidgetInterceptor
import com.caseyjbrooks.scripturenow.models.routing.ScriptureNowRoute
import com.caseyjbrooks.scripturenow.repositories.RepositoriesInjector
import com.caseyjbrooks.scripturenow.repositories.auth.AuthRepository
import com.caseyjbrooks.scripturenow.repositories.auth.AuthRepositoryImpl
import com.caseyjbrooks.scripturenow.repositories.auth.AuthRepositoryInputHandler
import com.caseyjbrooks.scripturenow.repositories.memory.MemoryVerseRepository
import com.caseyjbrooks.scripturenow.repositories.memory.MemoryVerseRepositoryImpl
import com.caseyjbrooks.scripturenow.repositories.memory.MemoryVerseRepositoryInputHandler
import com.caseyjbrooks.scripturenow.repositories.prayer.PrayerRepository
import com.caseyjbrooks.scripturenow.repositories.prayer.PrayerRepositoryImpl
import com.caseyjbrooks.scripturenow.repositories.prayer.PrayerRepositoryInputHandler
import com.caseyjbrooks.scripturenow.repositories.routing.ScriptureNowRouter
import com.caseyjbrooks.scripturenow.repositories.votd.VerseOfTheDayRepository
import com.caseyjbrooks.scripturenow.repositories.votd.VerseOfTheDayRepositoryImpl
import com.caseyjbrooks.scripturenow.repositories.votd.VerseOfTheDayRepositoryInputHandler
import com.caseyjbrooks.scripturenow.utils.models.votd.VerseOfTheDayToMemoryVerseConverterImpl
import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.AndroidLogger
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.BootstrapInterceptor
import com.copperleaf.ballast.core.LoggingInterceptor
import com.copperleaf.ballast.eventHandler
import com.copperleaf.ballast.navigation.routing.*
import com.copperleaf.ballast.navigation.vm.Router
import com.copperleaf.ballast.navigation.vm.withRouter
import com.copperleaf.ballast.plusAssign

class RepositoriesInjectorImpl(
    private val appInjector: AppInjector,
    private val dataSourcesInjector: DataSourcesInjectorImpl,
) : RepositoriesInjector {

    private fun getRepositoryBuilder(): BallastViewModelConfiguration.Builder {
        return BallastViewModelConfiguration.Builder()
            .apply {
                if (dataSourcesInjector.localAppConfig.logRepositories) {
                    this += LoggingInterceptor()
                }
                logger = { AndroidLogger("${dataSourcesInjector.localAppConfig.logPrefix} - $it") }
            }
    }

    private val _authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(
            coroutineScope = appInjector.appCoroutineScope,
            configBuilder = getRepositoryBuilder(),
            inputHandler = AuthRepositoryInputHandler(dataSourcesInjector.getSession()),
        )
    }
    private val _memoryVerseRepository: MemoryVerseRepository by lazy {
        MemoryVerseRepositoryImpl(
            coroutineScope = appInjector.appCoroutineScope,
            configBuilder = getRepositoryBuilder(),
            inputHandler = MemoryVerseRepositoryInputHandler(
                db = dataSourcesInjector.getMemoryVerseDb(),
                verseOfTheDayToMemoryVerseConverter = VerseOfTheDayToMemoryVerseConverterImpl(),
            ),
        )
    }
    private val _prayerRepository: PrayerRepository by lazy {
        PrayerRepositoryImpl(
            coroutineScope = appInjector.appCoroutineScope,
            configBuilder = getRepositoryBuilder(),
            inputHandler = PrayerRepositoryInputHandler(dataSourcesInjector.getPrayerDb()),
        )
    }
    private lateinit var _router: Router<ScriptureNowRoute>
    private val _verseOfTheDayRepository: VerseOfTheDayRepository by lazy {
        VerseOfTheDayRepositoryImpl(
            coroutineScope = appInjector.appCoroutineScope,
            configBuilder = getRepositoryBuilder()
                .apply {
                    this += VerseOfTheDayWidgetInterceptor(appInjector.applicationContext)
                },
            inputHandler = VerseOfTheDayRepositoryInputHandler(
                api = dataSourcesInjector.getVerseOfTheDayApi(),
                db = dataSourcesInjector.getVerseOfTheDayDb(),
            ),
        )
    }

    override fun getAuthRepository(): AuthRepository {
        return _authRepository
    }

    override fun getMemoryVerseRepository(): MemoryVerseRepository {
        return _memoryVerseRepository
    }

    override fun getPrayerRepository(): PrayerRepository {
        return _prayerRepository
    }

    override fun getScriptureNowRouter(deepLinkUrl: String?): ScriptureNowRouter {
        if (!::_router.isInitialized) {
            _router = BasicViewModel(
                config = getRepositoryBuilder()
                    .withRouter(RoutingTable.fromEnum(ScriptureNowRoute.values()), null)
                    .apply {
                        this += BootstrapInterceptor {
                            RouterContract.Inputs.GoToDestination<ScriptureNowRoute>(
                                deepLinkUrl ?: ScriptureNowRoute.Home.directions().build()
                            )
                        }
                    }
                    .build(),
                coroutineScope = appInjector.appCoroutineScope,
                eventHandler = eventHandler { },
            )
        } else {
            if (deepLinkUrl != null) {
                _router.trySend(
                    RouterContract.Inputs.GoToDestination(deepLinkUrl)
                )
            }
        }

        return _router
    }

    override fun getVerseOfTheDayRepository(): VerseOfTheDayRepository {
        return _verseOfTheDayRepository
    }
}
