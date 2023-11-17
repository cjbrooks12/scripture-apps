package com.caseyjbrooks.prayer.ui.timer

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

public object PrayerTimerUi {
    @Composable
    public fun Content(prayerId: String) {
        Text("Prayer Timer: PrayerId=$prayerId")
    }
}
