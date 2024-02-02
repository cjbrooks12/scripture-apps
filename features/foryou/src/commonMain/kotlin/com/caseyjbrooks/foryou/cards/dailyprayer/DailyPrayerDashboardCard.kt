package com.caseyjbrooks.foryou.cards.dailyprayer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.caseyjbrooks.prayer.models.DailyPrayer
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import com.copperleaf.ballast.repository.cache.isLoading

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DailyPrayerDashboardCard(
    dailyPrayer: Cached<DailyPrayer>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth().wrapContentHeight(),
        onClick = onClick,
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                "Daily Prayer",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 24.dp),
            )

            if (dailyPrayer.isLoading()) {
                Box(Modifier.fillMaxWidth().height(72.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                val prayer = dailyPrayer.getCachedOrNull()
                if (prayer == null) {
                    Text("Error getting today's daily prayer")
                } else {
                    Text(prayer.text)
                }
            }
        }
    }
}
