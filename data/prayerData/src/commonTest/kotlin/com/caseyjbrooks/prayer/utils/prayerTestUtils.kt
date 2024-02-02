package com.caseyjbrooks.prayer.utils

import com.caseyjbrooks.database.HardcodedUuidFactory
import com.caseyjbrooks.prayer.models.PrayerId
import com.caseyjbrooks.prayer.models.PrayerNotification
import com.caseyjbrooks.prayer.models.SavedPrayer
import com.caseyjbrooks.prayer.models.SavedPrayerType
import kotlinx.datetime.Clock

fun getPrayer(id: Int, text: String, clock: Clock = TestClock()): SavedPrayer {
    val instant = clock.now()
    return SavedPrayer(
        uuid = PrayerId(id),
        text = text,
        prayerType = SavedPrayerType.Persistent,
        tags = listOf(),
        archived = false,
        archivedAt = null,
        notification = PrayerNotification.None,
        createdAt = instant,
        updatedAt = instant,
    )
}

fun PrayerId(id: Int): PrayerId {
    return PrayerId(
        HardcodedUuidFactory(id).getNewUuid()
    )
}
