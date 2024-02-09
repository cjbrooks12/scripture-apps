package com.caseyjbrooks.prayer.domain.getwithnotifications

import com.caseyjbrooks.prayer.models.SavedPrayer

/**
 * Retrieve the list 0f prayers which are configured to send notifications, and which are not archived. These prayers
 * will be scheduled to display their notifications.
 */
public interface GetPrayersWithNotificationsUseCase {
    public suspend operator fun invoke(): List<SavedPrayer>
}
