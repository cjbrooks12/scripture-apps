package com.copperleaf.biblebits

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val intent = getIntent()
        val redirectContent = if (Intent.ACTION_VIEW == intent.action) {
            val uri: Uri = intent.data!!
            Log.i("Android", "uri.queryParameterNames: ${uri.queryParameterNames}")
            val code = uri.getQueryParameter("code")
            code
        } else {
            null
        }

        setContent {
            App(
                AndroidPlatform(applicationContext, redirectContent),
            )
        }
    }
}

