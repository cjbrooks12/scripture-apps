package com.copperleaf.biblebits

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.caseyjbrooks.dto.HealthCheckResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.coroutines.launch

@Composable
fun App(
    platform: Platform,
) {
    MaterialTheme {
        Column(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            var key: Int by remember { mutableStateOf(0) }
            var scopes: List<String> by remember { mutableStateOf(listOf("api:protected")) }

            Text("Hello, ${platform.name}!")
            Text("key: $key")
            HorizontalDivider()

            HttpRequest<HealthCheckResponse>(
                platform,
                path = "/api/v1/public/health",
                key = key
            )
            HorizontalDivider()

            HttpRequest<HealthCheckResponse>(
                platform,
                path = "/api/v1/protected/health",
                key = key
            )
            HorizontalDivider()

            HttpRequest<HealthCheckResponse>(
                platform,
                path = "/api/v1/secure/health",
                key = key
            )
            HorizontalDivider()

            HttpRequest<HealthCheckResponse>(
                platform,
                path = "/api/v1/admin/health",
                key = key
            )
            HorizontalDivider()

            RetryApisButton(
                onClick = { key++ }
            )
            LogInButton(platform, scopes)
            LogOutButton(platform)

            Text("Token Scopes")
            listOf(
                "api:protected",
                "api:secure",
                "api:admin",
            ).forEach { scope ->
                Row {
                    Checkbox(
                        checked = scope in scopes,
                        onCheckedChange = {
                            if(it) {
                                scopes += scope
                            } else {
                                scopes -= scope
                            }
                        }
                    )
                    Text(scope)
                }
            }
        }
    }
}

@Composable
private inline fun <reified T> HttpRequest(
    platform: Platform,
    path: String,
    key: Int,
)  {
    val response by produceState<Pair<HttpResponse?, T?>>(
        initialValue = null to null,
        key1 = key,
    ) {
        val res = platform.httpClient.get(path)
        value = res to runCatching { res.body<T>() }.getOrNull()
    }
    val (publicResponse, publicBody) = response

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(if(publicResponse?.status?.isSuccess() == true) Color.Green.copy(alpha = 0.1f) else Color.Red.copy(alpha = 0.1f))
    ) {
        Text(path, style = MaterialTheme.typography.bodyLarge)
        publicResponse?.let { response ->
            Text("Status Code: ${response.status}")
        }
        publicBody?.let { body ->
            Text("Body: $body")
        }
    }
}

@Composable
private fun RetryApisButton(
    onClick: () -> Unit,
) {
    Button(onClick = onClick) {
        Text("Retry APIs")
    }
}

@Composable
private fun LogInButton(
    platform: Platform,
    scopes: List<String>,
) {
    val coroutineScope = rememberCoroutineScope()
    Button(onClick = {
        coroutineScope.launch {
            platform.authService.requestLogIn(scopes)
        }
    }) {
        Text("Log In")
    }
}

@Composable
private fun LogOutButton(
    platform: Platform,
) {
    val coroutineScope = rememberCoroutineScope()
    Button(onClick = {
        coroutineScope.launch {
            platform.authService.requestLogOut()
        }
    }) {
        Text("Log Out")
    }
}

