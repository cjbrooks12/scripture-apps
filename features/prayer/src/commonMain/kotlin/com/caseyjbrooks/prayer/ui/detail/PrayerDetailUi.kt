package com.caseyjbrooks.prayer.ui.detail

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

public object PrayerDetailUi {
    @Composable
    public fun Content(prayerId: String) {
        Text("Prayer Detail: PrayerId=$prayerId")
    }
}
