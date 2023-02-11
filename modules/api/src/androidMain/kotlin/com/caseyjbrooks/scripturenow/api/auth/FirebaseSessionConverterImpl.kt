package com.caseyjbrooks.scripturenow.api.auth

import com.caseyjbrooks.scripturenow.models.auth.AuthState
import com.google.firebase.auth.FirebaseUser

class FirebaseSessionConverterImpl : FirebaseSessionConverter {
    override fun convertSdkModelToRepositoryModel(
        sdkModel: FirebaseUser
    ): AuthState = with(sdkModel) {
        AuthState.SignedIn(
            method = if (isAnonymous) {
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
