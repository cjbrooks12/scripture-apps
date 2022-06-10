package com.copperleaf.scripturenow.api.auth

import com.copperleaf.scripturenow.repositories.auth.models.AuthState
import com.google.firebase.auth.FirebaseUser


class FirebaseAuthenticationProviderConverterImpl : FirebaseAuthenticationProviderConverter {
    override fun convertSdkModelToRepositoryModel(
        sdkModel: FirebaseUser
    ): AuthState = with(sdkModel) {
        AuthState.SignedIn(
            method = if(isAnonymous) {
                AuthState.AuthenticationMethod.Anonymous
            } else {
                AuthState.AuthenticationMethod.EmailAndPassword
            },
            displayName = displayName ?: "",
            email = email ?: "",
            isEmailVerified = isEmailVerified,
            photoUrl = photoUrl?.toString() ?: "",
        )
    }
}
