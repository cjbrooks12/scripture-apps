package com.copperleaf.abide

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(
    httpClient: HttpClient = httpClient()
) {
    MaterialTheme {
        Column(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val bodyText by produceState<String?>(null) {
                value = httpClient.get("/api/v1/public/health").bodyAsText()
            }

            Text(Greeting().greet())
            bodyText?.let {
                Text("bodyText: $bodyText")
            }
        }
    }
}
