package com.copperleaf.scripturenow.repositories.auth

import com.copperleaf.scripturenow.repositories.auth.models.AuthState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun clearAllCaches()
    fun getAuthState(refreshCache: Boolean = false): Flow<AuthState>
    fun signOut()

}
