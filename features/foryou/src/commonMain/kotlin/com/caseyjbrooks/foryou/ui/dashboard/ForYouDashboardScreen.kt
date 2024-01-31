package com.caseyjbrooks.foryou.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.caseyjbrooks.foryou.cards.dailyprayer.DailyPrayerDashboardCard
import com.caseyjbrooks.foryou.cards.votd.VerseofTheDayDashboardCard
import com.caseyjbrooks.ui.koin.LocalKoin
import org.koin.core.parameter.parametersOf

public object ForYouDashboardScreen {
    @Composable
    public fun Content() {
        val koin = LocalKoin.current
        val coroutineScope = rememberCoroutineScope()
        val viewModel: ForYouDashboardViewModel = remember(coroutineScope, koin) {
            koin.get { parametersOf(coroutineScope) }
        }

        val uiState by viewModel.observeStates().collectAsState()

        Content(uiState) { viewModel.trySend(it) }
    }

    @Composable
    internal fun Content(
        uiState: ForYouDashboardContract.State,
        postInput: (ForYouDashboardContract.Inputs) -> Unit,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            VerseofTheDayDashboardCard(
                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                verseOfTheDay = uiState.verseOfTheDay,
                onClick = { postInput(ForYouDashboardContract.Inputs.VerseOfTheDayCardClicked) },
            )
            DailyPrayerDashboardCard(
                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                verseOfTheDay = uiState.dailyPrayer,
                onClick = { postInput(ForYouDashboardContract.Inputs.VerseOfTheDayCardClicked) },
            )
        }
    }
}
