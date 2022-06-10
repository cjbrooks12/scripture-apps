package com.copperleaf.scripturenow.repositories.auth

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.core.BootstrapInterceptor
import com.copperleaf.ballast.forViewModel
import com.copperleaf.ballast.plusAssign
import com.copperleaf.ballast.repository.BallastRepository
import com.copperleaf.ballast.repository.bus.EventBus
import com.copperleaf.ballast.repository.withRepository
import com.copperleaf.scripturenow.repositories.auth.models.AuthState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepositoryImpl(
    coroutineScope: CoroutineScope,
    eventBus: EventBus,
    configBuilder: BallastViewModelConfiguration.Builder,
    inputHandler: AuthRepositoryInputHandler,
) : BallastRepository<
    AuthRepositoryContract.Inputs,
    AuthRepositoryContract.State>(
    coroutineScope = coroutineScope,
    eventBus = eventBus,
    config = configBuilder
        .apply {
            this += BootstrapInterceptor {
                AuthRepositoryContract.Inputs.Initialize
            }
        }
        .withRepository()
        .forViewModel(
            inputHandler = inputHandler,
            initialState = AuthRepositoryContract.State(),
            name = "Auth Repository",
        ),
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
