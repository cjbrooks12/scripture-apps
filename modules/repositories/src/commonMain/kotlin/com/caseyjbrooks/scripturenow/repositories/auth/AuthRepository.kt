package com.caseyjbrooks.scripturenow.repositories.auth

import com.caseyjbrooks.scripturenow.models.auth.AuthState
import kotlinx.coroutines.flow.Flow

public interface AuthRepository {

    public fun clearAllCaches()
    public fun getAuthState(refreshCache: Boolean = false): Flow<AuthState>
    public fun signOut()
}
