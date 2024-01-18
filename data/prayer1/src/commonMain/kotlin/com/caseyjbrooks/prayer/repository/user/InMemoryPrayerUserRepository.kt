package com.caseyjbrooks.prayer.repository.user

import com.caseyjbrooks.prayer.models.PrayerUser

public class InMemoryPrayerUserRepository(
    private val hardcodedUser: PrayerUser?,
) : PrayerUserRepository {
    override suspend fun getUserProfile(): PrayerUser? {
        return hardcodedUser
    }
}
