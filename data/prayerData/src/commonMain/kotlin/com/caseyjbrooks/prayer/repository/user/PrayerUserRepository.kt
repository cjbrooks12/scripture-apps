package com.caseyjbrooks.prayer.repository.user

import com.caseyjbrooks.prayer.models.PrayerUser

public interface PrayerUserRepository {
    public suspend fun getUserProfile(): PrayerUser?
}
