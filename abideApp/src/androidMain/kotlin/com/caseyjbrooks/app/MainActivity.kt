package com.caseyjbrooks.app

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.caseyjbrooks.di.GlobalKoinApplication
import com.caseyjbrooks.domain.bus.EventBus
import com.caseyjbrooks.notifications.PermissionsDomainEvents
import com.caseyjbrooks.ui.ApplicationRoot
import org.koin.dsl.bind
import org.koin.dsl.module

class MainActivity : ComponentActivity() {

    private val module by lazy {
        module {
            factory<ComponentActivity> { this@MainActivity }.bind<Activity>()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        GlobalKoinApplication.loadModules(module)

        val deepLinkUrl: String? = intent?.data?.toString()?.removePrefix("scripture://now")
        intent?.data = null

        setContent {
            ApplicationRoot(GlobalKoinApplication.koin, deepLinkUrl)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        GlobalKoinApplication.unloadModules(module)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val eventBus: EventBus = GlobalKoinApplication.get()

        val permissionType = PermissionsDomainEvents.PermissionType
            .entries
            .getOrNull(requestCode)
            ?: PermissionsDomainEvents.PermissionType.None

        val result = if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            PermissionsDomainEvents.Result.Granted
        } else {
            PermissionsDomainEvents.Result.Denied
        }

        eventBus.trySend(PermissionsDomainEvents.PermissionResult(permissionType, result))
    }
}
