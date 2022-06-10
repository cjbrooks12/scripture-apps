package com.copperleaf.scripturenow.api.auth

import com.copperleaf.scripturenow.repositories.auth.models.AuthState
import com.google.firebase.auth.FirebaseUser


interface FirebaseAuthenticationProviderConverter {
    fun convertSdkModelToRepositoryModel(
        sdkModel: FirebaseUser
    ) : AuthState
}
