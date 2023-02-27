package com.caseyjbrooks.app.di

import com.caseyjbrooks.scripturenow.appwidgets.memory.MemoryVerseWidgetInterceptor
import com.caseyjbrooks.scripturenow.appwidgets.votd.VerseOfTheDayWidgetInterceptor
import com.caseyjbrooks.scripturenow.models.routing.ScriptureNowRoute
import com.caseyjbrooks.scripturenow.repositories.AndroidAssetsFormLoader
import com.caseyjbrooks.scripturenow.repositories.RepositoriesInjector
import com.caseyjbrooks.scripturenow.repositories.auth.AuthRepository
import com.caseyjbrooks.scripturenow.repositories.auth.AuthRepositoryImpl
import com.caseyjbrooks.scripturenow.repositories.auth.AuthRepositoryInputHandler
import com.caseyjbrooks.scripturenow.repositories.memory.MemoryVerseRepository
import com.caseyjbrooks.scripturenow.repositories.memory.MemoryVerseRepositoryImpl
import com.caseyjbrooks.scripturenow.repositories.memory.MemoryVerseRepositoryInputHandler
import com.caseyjbrooks.scripturenow.repositories.notifications.AndroidNotificationsEventHandler
import com.caseyjbrooks.scripturenow.repositories.notifications.NotificationsRepository
import com.caseyjbrooks.scripturenow.repositories.notifications.NotificationsRepositoryImpl
import com.caseyjbrooks.scripturenow.repositories.notifications.NotificationsRepositoryInputHandler
import com.caseyjbrooks.scripturenow.repositories.prayer.PrayerRepository
import com.caseyjbrooks.scripturenow.repositories.prayer.PrayerRepositoryImpl
import com.caseyjbrooks.scripturenow.repositories.prayer.PrayerRepositoryInputHandler
import com.caseyjbrooks.scripturenow.repositories.routing.ScriptureNowRouter
import com.caseyjbrooks.scripturenow.repositories.votd.VerseOfTheDayRepository
import com.caseyjbrooks.scripturenow.repositories.votd.VerseOfTheDayRepositoryContract
import com.caseyjbrooks.scripturenow.repositories.votd.VerseOfTheDayRepositoryImpl
import com.caseyjbrooks.scripturenow.repositories.votd.VerseOfTheDayRepositoryInputHandler
import com.caseyjbrooks.scripturenow.utils.models.votd.VerseOfTheDayToMemoryVerseConverterImpl
import com.copperleaf.ballast.*
import com.copperleaf.ballast.core.AndroidLogger
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.BootstrapInterceptor
import com.copperleaf.ballast.core.LoggingInterceptor
import com.copperleaf.ballast.navigation.routing.*
import com.copperleaf.ballast.navigation.vm.Router
import com.copperleaf.ballast.navigation.vm.withRouter
import kotlinx.coroutines.Dispatchers

class RepositoriesInjectorImpl(
    private val appInjector: AppInjector,
    private val dataSourcesInjector: DataSourcesInjectorImpl,
) : RepositoriesInjector {

    private fun getRepositoryBuilder(): BallastViewModelConfiguration.Builder {
        return BallastViewModelConfiguration.Builder()
            .dispatchers(
                inputsDispatcher = Dispatchers.Default,
                eventsDispatcher = Dispatchers.Default,
                sideJobsDispatcher = Dispatchers.IO,
                interceptorDispatcher = Dispatchers.Default
            )
            .apply {
                if (dataSourcesInjector.localAppConfig.logRepositories) {
                    this += LoggingInterceptor()
                }
                logger = { AndroidLogger("${dataSourcesInjector.localAppConfig.logPrefix} - $it") }
            }
    }

    private lateinit var _router: Router<ScriptureNowRoute>

    private val _authRepository: AuthRepository = AuthRepositoryImpl(
        coroutineScope = appInjector.appCoroutineScope,
        configBuilder = getRepositoryBuilder(),
        inputHandler = AuthRepositoryInputHandler(
            session = dataSourcesInjector.getSession(),
            preferences = dataSourcesInjector.getAppPreferences(),
        ),
    )
    private val _memoryVerseRepository: MemoryVerseRepository = MemoryVerseRepositoryImpl(
        coroutineScope = appInjector.appCoroutineScope,
        configBuilder = getRepositoryBuilder()
            .apply {
                this += MemoryVerseWidgetInterceptor(appInjector.applicationContext)
            },
        inputHandler = MemoryVerseRepositoryInputHandler(
            db = dataSourcesInjector.getMemoryVerseDb(),
            verseOfTheDayToMemoryVerseConverter = VerseOfTheDayToMemoryVerseConverterImpl(),
        ),
        memoryVerseFormLoader = AndroidAssetsFormLoader(appInjector.applicationContext, "memory"),
    )
    private val _notificationsRepository: NotificationsRepository = NotificationsRepositoryImpl(
        coroutineScope = appInjector.appCoroutineScope,
        configBuilder = getRepositoryBuilder(),
        inputHandler = NotificationsRepositoryInputHandler(
            memoryVerseRepository = _memoryVerseRepository,
            appPreferences = dataSourcesInjector.getAppPreferences(),
        ),
        eventHandler = AndroidNotificationsEventHandler(appInjector.applicationContext),
    )
    private val _prayerRepository: PrayerRepository = PrayerRepositoryImpl(
        coroutineScope = appInjector.appCoroutineScope,
        configBuilder = getRepositoryBuilder(),
        inputHandler = PrayerRepositoryInputHandler(dataSourcesInjector.getPrayerDb()),
        prayerFormLoader = AndroidAssetsFormLoader(appInjector.applicationContext, "prayer"),
    )
    private val _verseOfTheDayRepository: VerseOfTheDayRepository = VerseOfTheDayRepositoryImpl(
        coroutineScope = appInjector.appCoroutineScope,
        configBuilder = getRepositoryBuilder()
            .apply {
                this += VerseOfTheDayWidgetInterceptor(appInjector.applicationContext)
                this += BootstrapInterceptor { VerseOfTheDayRepositoryContract.Inputs.Initialize }
            },
        inputHandler = VerseOfTheDayRepositoryInputHandler(
            appPreferences = dataSourcesInjector.getAppPreferences(),
            api = dataSourcesInjector::getVerseOfTheDayApi,
            db = dataSourcesInjector.getVerseOfTheDayDb(),
        ),
    )

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
                eventHandler = eventHandler {
                    when (it) {
                        is RouterContract.Events.BackstackEmptied -> {
                            backstackEmptiedCallbacks.values.forEach { it() }
                        }

                        else -> {}
                    }
                },
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

    override fun getNotificationsRepository(): NotificationsRepository {
        return _notificationsRepository
    }

    private var backstackEmptiedCallbacks = mutableMapOf<Any, () -> Unit>()
    override fun registerBackstackEmptiedCallback(owner: Any, block: () -> Unit) {
        backstackEmptiedCallbacks += owner to block
    }

    override fun unregisterBackstackEmptiedCallback(owner: Any) {
        backstackEmptiedCallbacks.remove(owner)
    }
}
