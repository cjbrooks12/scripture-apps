package com.caseyjbrooks.foryou.cards.votd

import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.caseyjbrooks.votd.models.VerseOfTheDay
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.getCachedOrNull

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun VerseofTheDayDashboardCard(
    onClick: () -> Unit,
    verseOfTheDay: Cached<VerseOfTheDay>,
    modifier: Modifier = Modifier,
) {
    Card(onClick = onClick, modifier = modifier) {
        Text(verseOfTheDay.getCachedOrNull()?.verse ?: "Loading...")
        Text(verseOfTheDay.getCachedOrNull()?.reference ?: "")
    }
}
