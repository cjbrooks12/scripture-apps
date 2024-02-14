package com.caseyjbrooks.prayer.domain

public sealed interface PrayerDomainEvents {
    public data object PrayerAddedOrChanged : PrayerDomainEvents
}
