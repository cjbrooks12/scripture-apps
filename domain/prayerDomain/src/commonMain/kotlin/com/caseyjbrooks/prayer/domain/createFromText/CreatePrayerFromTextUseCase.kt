package com.caseyjbrooks.prayer.domain.createFromText

import com.caseyjbrooks.prayer.models.SavedPrayer
import com.caseyjbrooks.prayer.repository.config.PrayerConfig
import kotlinx.datetime.Instant

/**
 * Create a new prayer from user-generated input and store it in the DB.
 *
 * If the user is on a free plan, the user is limited to at most [PrayerConfig.maxPrayersOnFreePlan] saved prayers.
 * If they want more, they must subscribe, or delete some other prayers. This method will throw an exception if the
 * user would exceed that limit.
 */
public interface CreatePrayerFromTextUseCase {
    public suspend operator fun invoke(
        text: String,
        completionDate: Instant?,
        tags: Set<String>
    ): SavedPrayer
}
