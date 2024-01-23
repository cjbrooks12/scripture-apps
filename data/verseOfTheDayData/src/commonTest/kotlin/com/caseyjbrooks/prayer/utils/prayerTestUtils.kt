package com.caseyjbrooks.prayer.utils

import com.caseyjbrooks.prayer.models.PrayerId
import com.caseyjbrooks.prayer.models.SavedPrayer
import com.caseyjbrooks.prayer.models.SavedPrayerType
import kotlinx.datetime.Clock

fun getPrayer(id: String, text: String, clock: Clock = TestClock()): SavedPrayer {
    val instant = clock.now()
    return SavedPrayer(
        uuid = PrayerId(id),
        text = text,
        prayerType = SavedPrayerType.Persistent,
        tags = listOf(),
        archived = false,
        archivedAt = null,
        createdAt = instant,
        updatedAt = instant,
    )
}
