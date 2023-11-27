package com.caseyjbrooks.prayer.domain.create

import com.caseyjbrooks.prayer.models.SavedPrayer
import com.caseyjbrooks.prayer.repository.config.PrayerConfig

/**
 * Create a new prayer and insert it into the DB through.
 *
 * If the user is on a free plan, the user is limited to at most [PrayerConfig.maxPrayersOnFreePlan] saved prayers.
 * If they want more, they must subscribe, or delete some other prayers. This method will throw an exception if the
 * user would exceed that limit.
 */
public typealias CreatePrayerUseCase = suspend (prayer: SavedPrayer) -> SavedPrayer
