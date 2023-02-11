package com.caseyjbrooks.scripturenow.api.auth

import com.caseyjbrooks.scripturenow.models.auth.AuthState
import com.google.firebase.auth.FirebaseUser

interface FirebaseSessionConverter {
    fun convertSdkModelToRepositoryModel(
        sdkModel: FirebaseUser
    ): AuthState
}
