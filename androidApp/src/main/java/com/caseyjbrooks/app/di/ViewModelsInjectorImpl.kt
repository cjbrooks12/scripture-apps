package com.caseyjbrooks.app.di

import com.caseyjbrooks.scripturenow.repositories.RepositoriesInjector
import com.caseyjbrooks.scripturenow.viewmodel.ViewModelsInjector
import com.caseyjbrooks.scripturenow.viewmodel.home.HomeContract
import com.caseyjbrooks.scripturenow.viewmodel.home.HomeEventHandler
import com.caseyjbrooks.scripturenow.viewmodel.home.HomeInputHandler
import com.caseyjbrooks.scripturenow.viewmodel.home.HomeViewModel
import com.caseyjbrooks.scripturenow.viewmodel.memory.detail.MemoryVerseDetailsViewModel
import com.caseyjbrooks.scripturenow.viewmodel.memory.edit.CreateOrEditMemoryVerseViewModel
import com.caseyjbrooks.scripturenow.viewmodel.memory.list.MemoryVerseListViewModel
import com.caseyjbrooks.scripturenow.viewmodel.prayer.detail.PrayerDetailsViewModel
import com.caseyjbrooks.scripturenow.viewmodel.prayer.edit.CreateOrEditPrayerViewModel
import com.caseyjbrooks.scripturenow.viewmodel.prayer.list.PrayerListViewModel
import com.caseyjbrooks.scripturenow.viewmodel.settings.SettingsViewModel
import com.caseyjbrooks.scripturenow.viewmodel.votd.VerseOfTheDayContract
import com.caseyjbrooks.scripturenow.viewmodel.votd.VerseOfTheDayEventHandler
import com.caseyjbrooks.scripturenow.viewmodel.votd.VerseOfTheDayInputHandler
import com.caseyjbrooks.scripturenow.viewmodel.votd.VerseOfTheDayViewModel
import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.AndroidLogger
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.BootstrapInterceptor
import com.copperleaf.ballast.core.LoggingInterceptor
import com.copperleaf.ballast.plusAssign
import com.copperleaf.ballast.withViewModel
import kotlinx.coroutines.CoroutineScope

class ViewModelsInjectorImpl(
    private val appInjector: AppInjector,
) : ViewModelsInjector {
    private val dataSourcesInjector: DataSourcesInjectorImpl = appInjector.dataSourcesInjector
    private val repositories: RepositoriesInjector = appInjector.repositoriesInjector

    private fun getViewModelBuilder(): BallastViewModelConfiguration.Builder {
        return BallastViewModelConfiguration.Builder()
            .apply {
                if (dataSourcesInjector.localAppConfig.logViewModels) {
                    this += LoggingInterceptor()
                }
                logger = { AndroidLogger("${dataSourcesInjector.localAppConfig.logPrefix} - $it") }
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

    override fun getMemoryVerseDetailsViewModel(
        coroutineScope: CoroutineScope,
        verseId: String
    ): MemoryVerseDetailsViewModel {
        TODO("Not yet implemented")
    }

    override fun getCreateOrEditMemoryVerseViewModel(
        coroutineScope: CoroutineScope,
        verseId: String
    ): CreateOrEditMemoryVerseViewModel {
        TODO("Not yet implemented")
    }

    override fun getMemoryVerseListViewModel(
        coroutineScope: CoroutineScope,
    ): MemoryVerseListViewModel {
        TODO("Not yet implemented")
    }

    override fun getPrayerDetailsViewModel(
        coroutineScope: CoroutineScope,
        prayerId: String,
    ): PrayerDetailsViewModel {
        TODO("Not yet implemented")
    }

    override fun getCreateOrEditPrayerViewModel(
        coroutineScope: CoroutineScope,
        prayerId: String,
    ): CreateOrEditPrayerViewModel {
        TODO("Not yet implemented")
    }

    override fun getPrayerListViewModel(
        coroutineScope: CoroutineScope,
    ): PrayerListViewModel {
        TODO("Not yet implemented")
    }

    override fun getSettingsViewModel(
        coroutineScope: CoroutineScope,
    ): SettingsViewModel {
        TODO("Not yet implemented")
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
}
