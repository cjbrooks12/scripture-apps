package com.caseyjbrooks.app.di

import com.benasher44.uuid.Uuid
import com.caseyjbrooks.scripturenow.models.memory.MemoryVerse
import com.caseyjbrooks.scripturenow.models.prayer.Prayer
import com.caseyjbrooks.scripturenow.repositories.RepositoriesInjector
import com.caseyjbrooks.scripturenow.utils.converter.FromJsonConverter
import com.caseyjbrooks.scripturenow.utils.converter.ToJsonConverter
import com.caseyjbrooks.scripturenow.viewmodel.ViewModelsInjector
import com.caseyjbrooks.scripturenow.viewmodel.home.HomeContract
import com.caseyjbrooks.scripturenow.viewmodel.home.HomeEventHandler
import com.caseyjbrooks.scripturenow.viewmodel.home.HomeInputHandler
import com.caseyjbrooks.scripturenow.viewmodel.home.HomeViewModel
import com.caseyjbrooks.scripturenow.viewmodel.memory.detail.MemoryVerseDetailsContract
import com.caseyjbrooks.scripturenow.viewmodel.memory.detail.MemoryVerseDetailsEventHandler
import com.caseyjbrooks.scripturenow.viewmodel.memory.detail.MemoryVerseDetailsInputHandler
import com.caseyjbrooks.scripturenow.viewmodel.memory.detail.MemoryVerseDetailsViewModel
import com.caseyjbrooks.scripturenow.viewmodel.memory.edit.CreateOrEditMemoryVerseContract
import com.caseyjbrooks.scripturenow.viewmodel.memory.edit.CreateOrEditMemoryVerseEventHandler
import com.caseyjbrooks.scripturenow.viewmodel.memory.edit.CreateOrEditMemoryVerseInputHandler
import com.caseyjbrooks.scripturenow.viewmodel.memory.edit.CreateOrEditMemoryVerseViewModel
import com.caseyjbrooks.scripturenow.viewmodel.memory.list.MemoryVerseListContract
import com.caseyjbrooks.scripturenow.viewmodel.memory.list.MemoryVerseListEventHandler
import com.caseyjbrooks.scripturenow.viewmodel.memory.list.MemoryVerseListInputHandler
import com.caseyjbrooks.scripturenow.viewmodel.memory.list.MemoryVerseListViewModel
import com.caseyjbrooks.scripturenow.viewmodel.prayer.detail.PrayerDetailsContract
import com.caseyjbrooks.scripturenow.viewmodel.prayer.detail.PrayerDetailsEventHandler
import com.caseyjbrooks.scripturenow.viewmodel.prayer.detail.PrayerDetailsInputHandler
import com.caseyjbrooks.scripturenow.viewmodel.prayer.detail.PrayerDetailsViewModel
import com.caseyjbrooks.scripturenow.viewmodel.prayer.edit.CreateOrEditPrayerContract
import com.caseyjbrooks.scripturenow.viewmodel.prayer.edit.CreateOrEditPrayerEventHandler
import com.caseyjbrooks.scripturenow.viewmodel.prayer.edit.CreateOrEditPrayerInputHandler
import com.caseyjbrooks.scripturenow.viewmodel.prayer.edit.CreateOrEditPrayerViewModel
import com.caseyjbrooks.scripturenow.viewmodel.prayer.list.PrayerListContract
import com.caseyjbrooks.scripturenow.viewmodel.prayer.list.PrayerListEventHandler
import com.caseyjbrooks.scripturenow.viewmodel.prayer.list.PrayerListInputHandler
import com.caseyjbrooks.scripturenow.viewmodel.prayer.list.PrayerListViewModel
import com.caseyjbrooks.scripturenow.viewmodel.settings.SettingsContract
import com.caseyjbrooks.scripturenow.viewmodel.settings.SettingsEventHandler
import com.caseyjbrooks.scripturenow.viewmodel.settings.SettingsInputHandler
import com.caseyjbrooks.scripturenow.viewmodel.settings.SettingsViewModel
import com.caseyjbrooks.scripturenow.viewmodel.votd.VerseOfTheDayContract
import com.caseyjbrooks.scripturenow.viewmodel.votd.VerseOfTheDayEventHandler
import com.caseyjbrooks.scripturenow.viewmodel.votd.VerseOfTheDayInputHandler
import com.caseyjbrooks.scripturenow.viewmodel.votd.VerseOfTheDayViewModel
import com.copperleaf.ballast.*
import com.copperleaf.ballast.core.AndroidLogger
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.BootstrapInterceptor
import com.copperleaf.ballast.core.LoggingInterceptor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class ViewModelsInjectorImpl(
    private val appInjector: AppInjector,
) : ViewModelsInjector {
    private val repositories: RepositoriesInjector = appInjector.repositoriesInjector

    private fun getViewModelBuilder(): BallastViewModelConfiguration.Builder {
        return BallastViewModelConfiguration.Builder()
            .dispatchers(
                inputsDispatcher = Dispatchers.Main.immediate,
                eventsDispatcher = Dispatchers.Main.immediate,
                sideJobsDispatcher = Dispatchers.IO,
                interceptorDispatcher = Dispatchers.Default
            )
            .apply {
                if (appInjector.configInjector.getLocalAppConfig().logViewModels) {
                    this += LoggingInterceptor()
                }
                logger = { AndroidLogger("${appInjector.configInjector.getLocalAppConfig().logPrefix} - $it") }
            }
    }

    override fun getHomeViewModel(
        coroutineScope: CoroutineScope,
    ): HomeViewModel {
        return BasicViewModel(
            config = getViewModelBuilder()
                .withViewModel(
                    initialState = HomeContract.State(),
                    inputHandler = HomeInputHandler(
                        verseOfTheDayRepository = repositories.getVerseOfTheDayRepository(),
                        memoryVerseRepository = repositories.getMemoryVerseRepository(),
                    ),
                    name = "Home"
                )
                .apply {
                    this += BootstrapInterceptor {
                        HomeContract.Inputs.Initialize
                    }
                }
                .build(),
            coroutineScope = coroutineScope,
            eventHandler = HomeEventHandler(repositories.getScriptureNowRouter(null)),
        )
    }

    override fun getSettingsViewModel(
        coroutineScope: CoroutineScope,
    ): SettingsViewModel {
        return BasicViewModel(
            config = getViewModelBuilder()
                .withViewModel(
                    initialState = SettingsContract.State(),
                    inputHandler = SettingsInputHandler(
                        globalRepository = repositories.getGlobalRepository(),
                    ),
                    name = "Settings"
                )
                .apply {
                    this += BootstrapInterceptor {
                        SettingsContract.Inputs.Initialize
                    }
                }
                .build(),
            coroutineScope = coroutineScope,
            eventHandler = SettingsEventHandler(repositories.getScriptureNowRouter(null)),
        )
    }

    override fun getVerseOfTheDayViewModel(
        coroutineScope: CoroutineScope,
    ): VerseOfTheDayViewModel {
        return BasicViewModel(
            config = getViewModelBuilder()
                .withViewModel(
                    initialState = VerseOfTheDayContract.State(),
                    inputHandler = VerseOfTheDayInputHandler(
                        verseOfTheDayRepository = repositories.getVerseOfTheDayRepository(),
                        memoryVerseRepository = repositories.getMemoryVerseRepository(),
                    ),
                    name = "Home"
                )
                .apply {
                    this += BootstrapInterceptor {
                        VerseOfTheDayContract.Inputs.Initialize(false)
                    }
                }
                .build(),
            coroutineScope = coroutineScope,
            eventHandler = VerseOfTheDayEventHandler(),
        )
    }

    override fun getMemoryVerseListViewModel(
        coroutineScope: CoroutineScope,
    ): MemoryVerseListViewModel {
        return BasicViewModel(
            config = getViewModelBuilder()
                .withViewModel(
                    initialState = MemoryVerseListContract.State(),
                    inputHandler = MemoryVerseListInputHandler(
                        memoryVerseRepository = repositories.getMemoryVerseRepository(),
                    ),
                    name = "MemoryVerseList"
                )
                .apply {
                    this += BootstrapInterceptor {
                        MemoryVerseListContract.Inputs.Initialize(false)
                    }
                }
                .build(),
            coroutineScope = coroutineScope,
            eventHandler = MemoryVerseListEventHandler(repositories.getScriptureNowRouter(null)),
        )
    }

    override fun getMemoryVerseDetailsViewModel(
        coroutineScope: CoroutineScope,
        verseId: String
    ): MemoryVerseDetailsViewModel {
        return BasicViewModel(
            config = getViewModelBuilder()
                .withViewModel(
                    initialState = MemoryVerseDetailsContract.State(),
                    inputHandler = MemoryVerseDetailsInputHandler(
                        memoryVerseRepository = repositories.getMemoryVerseRepository(),
                    ),
                    name = "MemoryVerseDetails"
                )
                .apply {
                    this += BootstrapInterceptor {
                        MemoryVerseDetailsContract.Inputs.Initialize(Uuid.fromString(verseId))
                    }
                }
                .build(),
            coroutineScope = coroutineScope,
            eventHandler = MemoryVerseDetailsEventHandler(repositories.getScriptureNowRouter(null)),
        )
    }

    override fun getCreateOrEditMemoryVerseViewModel(
        coroutineScope: CoroutineScope,
        verseId: String?
    ): CreateOrEditMemoryVerseViewModel {
        return BasicViewModel(
            config = getViewModelBuilder()
                .withViewModel(
                    initialState = CreateOrEditMemoryVerseContract.State(),
                    inputHandler = CreateOrEditMemoryVerseInputHandler(
                        memoryVerseRepository = repositories.getMemoryVerseRepository(),
                        fromJsonConverter = FromJsonConverter(MemoryVerse.serializer()),
                        toJsonConverter = ToJsonConverter(MemoryVerse.serializer()),
                    ),
                    name = "CreateOrEditMemoryVerse"
                )
                .apply {
                    this += BootstrapInterceptor {
                        CreateOrEditMemoryVerseContract.Inputs.Initialize(verseId?.let(Uuid::fromString))
                    }
                }
                .build(),
            coroutineScope = coroutineScope,
            eventHandler = CreateOrEditMemoryVerseEventHandler(repositories.getScriptureNowRouter(null)),
        )
    }

    override fun getPrayerListViewModel(
        coroutineScope: CoroutineScope,
    ): PrayerListViewModel {
        return BasicViewModel(
            config = getViewModelBuilder()
                .withViewModel(
                    initialState = PrayerListContract.State(),
                    inputHandler = PrayerListInputHandler(
                        prayerRepository = repositories.getPrayerRepository(),
                    ),
                    name = "PrayerList"
                )
                .apply {
                    this += BootstrapInterceptor {
                        PrayerListContract.Inputs.Initialize(false)
                    }
                }
                .build(),
            coroutineScope = coroutineScope,
            eventHandler = PrayerListEventHandler(repositories.getScriptureNowRouter(null)),
        )
    }

    override fun getPrayerDetailsViewModel(
        coroutineScope: CoroutineScope,
        prayerId: String,
    ): PrayerDetailsViewModel {
        return BasicViewModel(
            config = getViewModelBuilder()
                .withViewModel(
                    initialState = PrayerDetailsContract.State(),
                    inputHandler = PrayerDetailsInputHandler(
                        prayerRepository = repositories.getPrayerRepository(),
                    ),
                    name = "PrayerDetails"
                )
                .apply {
                    this += BootstrapInterceptor {
                        PrayerDetailsContract.Inputs.Initialize(Uuid.fromString(prayerId))
                    }
                }
                .build(),
            coroutineScope = coroutineScope,
            eventHandler = PrayerDetailsEventHandler(repositories.getScriptureNowRouter(null)),
        )
    }

    override fun getCreateOrEditPrayerViewModel(
        coroutineScope: CoroutineScope,
        prayerId: String?,
    ): CreateOrEditPrayerViewModel {
        return BasicViewModel(
            config = getViewModelBuilder()
                .withViewModel(
                    initialState = CreateOrEditPrayerContract.State(),
                    inputHandler = CreateOrEditPrayerInputHandler(
                        prayerRepository = repositories.getPrayerRepository(),
                        fromJsonConverter = FromJsonConverter(Prayer.serializer()),
                        toJsonConverter = ToJsonConverter(Prayer.serializer()),
                    ),
                    name = "CreateOrEditPrayer"
                )
                .apply {
                    this += BootstrapInterceptor {
                        CreateOrEditPrayerContract.Inputs.Initialize(prayerId?.let(Uuid::fromString))
                    }
                }
                .build(),
            coroutineScope = coroutineScope,
            eventHandler = CreateOrEditPrayerEventHandler(repositories.getScriptureNowRouter(null)),
        )
    }
}
