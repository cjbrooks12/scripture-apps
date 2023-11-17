package com.caseyjbrooks.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.caseyjbrooks.shared.MainCommonApplication

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        val deepLinkUrl: String? = intent?.data?.toString()?.removePrefix("scripture://now")
        intent?.data = null

        setContent {
            MainCommonApplication()
        }
    }
}
