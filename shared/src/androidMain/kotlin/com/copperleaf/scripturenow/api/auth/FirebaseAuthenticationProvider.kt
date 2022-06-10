package com.copperleaf.scripturenow.api.auth

import com.copperleaf.scripturenow.repositories.auth.models.AuthState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirebaseAuthenticationProvider(
    private val firebaseAuth: FirebaseAuth,
    private val converter: FirebaseAuthenticationProviderConverter,
) : AuthenticationProvider {
    override suspend fun getAuthState(): Flow<AuthState> {
        return callbackFlow {
            val callback = { authState: FirebaseAuth ->
                val currentUser = authState.currentUser
                if (currentUser != null) {
                    // user is logged in
                    trySend(
                        converter.convertSdkModelToRepositoryModel(currentUser)
                    )
                } else {
                    // not logged in
                    trySend(AuthState.SignedOut)
                }

                Unit
            }

            firebaseAuth.addAuthStateListener(callback)
            awaitClose {
                firebaseAuth.removeAuthStateListener(callback)
            }
        }
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }
}
