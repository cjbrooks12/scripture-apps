package com.caseyjbrooks.verses.models

/**
 * Details about the currently logged in user.
 */
public data class VerseUser(
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
