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
import com.caseyjbrooks.foryou.cards.notice.NoticeDashboardCard
import com.caseyjbrooks.foryou.cards.notifications.NotificationsCard
import com.caseyjbrooks.foryou.cards.overview.OverviewDashboardCard
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            if (uiState.showOverview) {
                OverviewDashboardCard(
                    modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                    onClick = { postInput(ForYouDashboardContract.Inputs.OverviewCardClicked) },
                )
            }

            if (uiState.noticeText != null) {
                NoticeDashboardCard(
                    noticeText = uiState.noticeText,
                    modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                    onClick = { postInput(ForYouDashboardContract.Inputs.NoticeCardClicked) },
                )
            }

            if (!uiState.notificationsEnabled) {
                NotificationsCard(
                    modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                    onClick = { postInput(ForYouDashboardContract.Inputs.ShowNotificationPermissionPrompt) },
                )
            }

            if (uiState.showVerseOfTheDay) {
                VerseofTheDayDashboardCard(
                    modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                    verseOfTheDay = uiState.verseOfTheDay,
                    onClick = { postInput(ForYouDashboardContract.Inputs.VerseOfTheDayCardClicked) },
                )
            }
            if (uiState.showDailyPrayer) {
                DailyPrayerDashboardCard(
                    modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                    dailyPrayer = uiState.dailyPrayer,
                    onClick = { postInput(ForYouDashboardContract.Inputs.DailyPrayerCardClicked) },
                )
            }
        }
    }
}
