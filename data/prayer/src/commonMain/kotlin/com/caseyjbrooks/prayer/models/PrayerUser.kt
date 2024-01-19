package com.caseyjbrooks.prayer.models

/**
 * Details about the currently logged in user.
 */
public data class PrayerUser(
    /**
     * The user's name
     */
    val name: String,

    /**
     * Whether the user is a paid subscriber
     */
    val subscription: SubscriptionStatus,
) {
    public enum class SubscriptionStatus {
        Free,
        Subscribed,
    }
}
