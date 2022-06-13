package com.caseyjbrooks.app.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.caseyjbrooks.app.utils.ComposeScreen
import com.caseyjbrooks.app.utils.theme.LocalInjector
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.scripturenow.repositories.auth.models.AuthState
import com.copperleaf.scripturenow.ui.Destinations
import com.copperleaf.scripturenow.ui.settings.SettingsContract

class SettingsScreen : ComposeScreen(Destinations.App.Settings) {

    @Composable
    override fun screenContent(destination: Destination): Content {
        val coroutineScope = rememberCoroutineScope()
        val injector = LocalInjector.current
        val vm = remember(coroutineScope, injector) { injector.settingsViewModel(coroutineScope) }
        val vmState by vm.observeStates().collectAsState()

        return rememberScrollableContent(
            appBarContent = {
                TopAppBar(
                    title = { Text("Settings") },
                    actions = {
                        Box(Modifier.size(40.dp)) {
                            (vmState.authState as? AuthState.SignedIn)?.let { user ->
                                AsyncImage(
                                    model = user.photoUrl,
                                    contentDescription = user.displayName
                                )
                            }
                        }
                    }
                )
            },
            mainContent = {
                Column(Modifier.wrapContentHeight(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    AuthenticationCard(vmState, vm::trySend)
                    DonatonCard(vmState, vm::trySend)
                    ReviewCard(vmState, vm::trySend)
                    AppUpdateCard(vmState, vm::trySend)
                }
            }
        )
    }

    @Composable
    private fun AuthenticationCard(
        vmState: SettingsContract.State,
        postInput: (SettingsContract.Inputs) -> Unit,
    ) {
        Card(elevation = 4.dp, modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (val authState = vmState.authState) {
                    is AuthState.SignedOut -> {
                        Button(
                            onClick = { postInput(SettingsContract.Inputs.SignIn) },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text("Sign In")
                        }
                    }
                    is AuthState.SignedIn -> {
                        val secondaryText =
                            if (authState.method == AuthState.AuthenticationMethod.Anonymous) {
                                "Complete Sign-Up"
                            } else if (authState.email.isNotBlank()) {
                                authState.email
                            } else {
                                null
                            }

                        ListItem(
                            modifier = Modifier
                                .clickable(enabled = authState.method == AuthState.AuthenticationMethod.Anonymous) {
                                    postInput(SettingsContract.Inputs.SignIn)
                                },
                            icon = {
                                Surface(
                                    Modifier.size(48.dp),
                                    color = MaterialTheme.colors.secondary,
                                    shape = CircleShape,
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize().padding(8.dp),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        if (
                                            authState.method == AuthState.AuthenticationMethod.Anonymous ||
                                            authState.photoUrl.isBlank()
                                        ) {
                                            Icon(
                                                Icons.Default.PersonOutline,
                                                contentDescription = authState.displayName
                                            )
                                        } else {
                                            AsyncImage(
                                                model = authState.photoUrl,
                                                contentDescription = authState.displayName
                                            )
                                        }
                                    }
                                }
                            },
                            text = {
                                if (authState.method == AuthState.AuthenticationMethod.Anonymous) {
                                    Text("Browsing as Guest")
                                } else {
                                    Text("Signed in as ${authState.displayName}")
                                }
                            },
                            secondaryText = secondaryText?.let {
                                { Text(it) }
                            },
                            trailing = {
                                if (authState.method == AuthState.AuthenticationMethod.Anonymous) {
                                    Icon(
                                        Icons.Default.ChevronRight,
                                        contentDescription = authState.displayName
                                    )
                                }
                            }
                        )

                        Button(
                            onClick = { postInput(SettingsContract.Inputs.SignOut) },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text("Sign Out")
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun DonatonCard(
        vmState: SettingsContract.State,
        postInput: (SettingsContract.Inputs) -> Unit,
    ) {
        Card(elevation = 4.dp, modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Support Scripture Now's development", style = MaterialTheme.typography.subtitle1)
                Text(
                    "This app takes considerable time and experience to develop, and is done entirely in " +
                        "the developer's free time. Running the servers is also expensive, so a one-time " +
                        "donation will greatly encourage me to continue development. A recurring donation " +
                        "will unlock bonus features for you, including automatic syncing to other devices."
                )

                Button(
                    onClick = { postInput(SettingsContract.Inputs.StartOneTimeDonation) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Donate $${vmState.selectedDonationAmount} one-time")
                }

                Button(
                    onClick = { postInput(SettingsContract.Inputs.StartRecurringDonation) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Start $${vmState.selectedDonationAmount} monthly subscription")
                }
            }
        }
    }

    @Composable
    private fun ReviewCard(
        vmState: SettingsContract.State,
        postInput: (SettingsContract.Inputs) -> Unit,
    ) {
        if (vmState.showReviewPrompt) {
            Card(elevation = 4.dp, modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Are you enjoying Scripture Now?", style = MaterialTheme.typography.subtitle1)
                    Text("Leave us a review and share this app with your friends!")

                    Button(
                        onClick = { postInput(SettingsContract.Inputs.ReviewButtonClicked) },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Review")
                    }

                    Button(
                        onClick = { postInput(SettingsContract.Inputs.ShareButtonClicked) },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Share")
                    }
                }
            }
        }
    }

    @Composable
    private fun AppUpdateCard(
        vmState: SettingsContract.State,
        postInput: (SettingsContract.Inputs) -> Unit,
    ) {
        if (vmState.updateIsAvailable) {
            Card(elevation = 4.dp, modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("An app update is available", style = MaterialTheme.typography.subtitle1)
                    Text("Please install the latest version of Scripture Now to enjoy the latest features and fixes.")
                    Text("Current Version: ${vmState.currentVersion}")
                    Text("Latest Version: ${vmState.latestVersion}")
                }
            }
        }
    }
}
