package com.copperleaf.biblebits

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val platform = AndroidPlatform(applicationContext)

        val intent = getIntent()
        val authorizationCode = if (Intent.ACTION_VIEW == intent.action) {
            intent.data?.getQueryParameter("code")
        } else {
            null
        }

        setContent {
            val appReady by produceState(false) {
                if(authorizationCode != null) {
                    platform.authService.getAuthToken(authorizationCode)
                }
                value = true
            }

            if(appReady) {
                App(platform)
            }
        }
    }
}

