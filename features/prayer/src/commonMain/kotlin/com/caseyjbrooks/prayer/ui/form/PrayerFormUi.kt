package com.caseyjbrooks.prayer.ui.form

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

public object PrayerFormUi {
    @Composable
    public fun Content(prayerId: String?) {
        Text("Prayer Form: PrayerId=$prayerId")
    }
}
