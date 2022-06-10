package com.copperleaf.scripturenow.api.auth

import com.copperleaf.scripturenow.repositories.auth.models.AuthState
import kotlinx.coroutines.flow.Flow

interface AuthenticationProvider {
    suspend fun getAuthState(): Flow<AuthState>
    suspend fun signOut()
}
