package com.caseyjbrooks.scripturenow.repositories.auth

import com.caseyjbrooks.scripturenow.models.auth.AuthState
import com.copperleaf.ballast.*
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.BootstrapInterceptor
import com.copperleaf.ballast.core.FifoInputStrategy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

public class AuthRepositoryImpl(
    coroutineScope: CoroutineScope,
    configBuilder: BallastViewModelConfiguration.Builder,
    inputHandler: AuthRepositoryInputHandler,
) : BasicViewModel<
    AuthRepositoryContract.Inputs,
    AuthRepositoryContract.Events,
    AuthRepositoryContract.State>(
    coroutineScope = coroutineScope,
    config = configBuilder
        .apply {
            this += BootstrapInterceptor {
                AuthRepositoryContract.Inputs.Initialize
            }
            inputStrategy = FifoInputStrategy()
        }
        .withViewModel(
            inputHandler = inputHandler,
            initialState = AuthRepositoryContract.State(),
            name = "Auth Repository",
        )
        .build(),
    eventHandler = eventHandler { }
), AuthRepository {
    override fun clearAllCaches() {
        trySend(AuthRepositoryContract.Inputs.ClearCaches)
    }

    override fun getAuthState(refreshCache: Boolean): Flow<AuthState> {
        return observeStates()
            .map { it.authState }
    }

    override fun signOut() {
        trySend(AuthRepositoryContract.Inputs.RequestSignOut)
    }
}
