package com.caseyjbrooks.foryou.cards.dailyprayer

import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.caseyjbrooks.prayer.models.DailyPrayer
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.getCachedOrNull

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DailyPrayerDashboardCard(
    verseOfTheDay: Cached<DailyPrayer>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(onClick = onClick, modifier = modifier) {
        Text(verseOfTheDay.getCachedOrNull()?.text ?: "Loading...")
    }
}
