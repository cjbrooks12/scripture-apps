package com.caseyjbrooks.scripturenow.notifications

import android.content.Context
import androidx.startup.Initializer
import com.caseyjbrooks.scripturenow.repositories.RepositoriesInjectorProvider
import com.google.firebase.installations.FirebaseInstallations

class FirebaseInstallationIdInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        println("FirebaseInstallationIdInitializer.create()")
        val mainInjector = (context.applicationContext as RepositoriesInjectorProvider).getRepositoriesInjector()
        FirebaseInstallations.getInstance().id.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                mainInjector.getAuthRepository().firebaseInstallationIdUpdated(task.result)
            }
        }
        FirebaseInstallations.getInstance().getToken(false).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                mainInjector.getAuthRepository().firebaseTokenUpdated(task.result.token)
            }
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        // No dependencies on other libraries.
        return emptyList()
    }
}
