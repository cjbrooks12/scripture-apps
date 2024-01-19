package com.caseyjbrooks.prayer.repository.config

/**
 * Values used to configure behavior of the application, which are not changed by the user. They may be hardcoded with
 * the application, or may be provided via remote configuration.
 */
public interface PrayerConfig {
    /**
     * The maximum number of prayers that can be saved if the user is on the free plan. If they're a subscriber, they
     * can save an unlimited number of prayers.
     */
    public val maxPrayersOnFreePlan: Int
}
