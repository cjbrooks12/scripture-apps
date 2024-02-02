package com.caseyjbrooks.foryou.cards.overview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun OverviewDashboardCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth().wrapContentHeight(),
        onClick = onClick,
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                "Welcome to Abide!",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 24.dp),
            )

            Text("This app helps you connect with Jesus and learn to simply Abide in Him. Memorize scripture, browse " +
                    "the topical Bible, or set up reminders to pray.")
        }
    }
}
