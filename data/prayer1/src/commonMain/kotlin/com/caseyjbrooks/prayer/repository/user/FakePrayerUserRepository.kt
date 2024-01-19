package com.caseyjbrooks.prayer.repository.user

import com.caseyjbrooks.prayer.models.PrayerUser

internal class FakePrayerUserRepository(
    private val hardcodedUser: PrayerUser?,
) : PrayerUserRepository {
    override suspend fun getUserProfile(): PrayerUser? {
        return hardcodedUser
    }
}
