package com.caseyjbrooks.scripturenow.api.auth

import com.caseyjbrooks.scripturenow.models.auth.AuthState
import kotlinx.coroutines.flow.Flow

public interface Session {
    public suspend fun getAuthState(): Flow<AuthState>
    public suspend fun signOut()
}
