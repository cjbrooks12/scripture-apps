package com.copperleaf.scripturenow.api.auth

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

fun androidAuthApiModule() = DI.Module(name = "API > Auth") {
    bind<AuthenticationProvider> {
        singleton {
            FirebaseAuthenticationProvider(
                firebaseAuth = Firebase.auth,
                converter = FirebaseAuthenticationProviderConverterImpl(),
            )
        }
    }
}
