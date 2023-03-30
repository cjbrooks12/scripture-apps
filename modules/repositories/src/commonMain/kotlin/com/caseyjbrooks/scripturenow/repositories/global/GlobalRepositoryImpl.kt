package com.caseyjbrooks.scripturenow.repositories.global

import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDayService
import com.copperleaf.ballast.*
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.BootstrapInterceptor
import com.copperleaf.ballast.core.FifoInputStrategy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

public class GlobalRepositoryImpl(
    coroutineScope: CoroutineScope,
    configBuilder: BallastViewModelConfiguration.Builder,
    inputHandler: GlobalRepositoryInputHandler,
) : BasicViewModel<
        GlobalRepositoryContract.Inputs,
        GlobalRepositoryContract.Events,
        GlobalRepositoryContract.State>(
    coroutineScope = coroutineScope,
    config = configBuilder
        .apply {
            this += BootstrapInterceptor {
                GlobalRepositoryContract.Inputs.Initialize
            }
            inputStrategy = FifoInputStrategy()
        }
        .withViewModel(
            inputHandler = inputHandler,
            initialState = GlobalRepositoryContract.State(),
            name = "Global Repository",
        )
        .build(),
    eventHandler = eventHandler { },
), GlobalRepository {

    override fun getGlobalState(): StateFlow<GlobalRepositoryContract.State> {
        return observeStates()
    }

    override fun signOut() {
        trySend(GlobalRepositoryContract.Inputs.RequestSignOut)
    }

    override fun setVerseOfTheDayService(value: VerseOfTheDayService) {
        trySend(GlobalRepositoryContract.Inputs.UpdateVerseOfTheDayService(value))
    }

    override fun setFirebaseInstallationId(value: String) {
        trySend(GlobalRepositoryContract.Inputs.UpdateFirebaseInstallationId(value))
    }

    override fun setFirebaseToken(value: String) {
        trySend(GlobalRepositoryContract.Inputs.UpdateFirebaseToken(value))
    }

    override fun setShowMainVerse(value: Boolean) {
        trySend(GlobalRepositoryContract.Inputs.UpdateShowMainVerse(value))
    }

    override fun checkForUpdates() {
        trySend(GlobalRepositoryContract.Inputs.CheckForUpdates)
    }
}
