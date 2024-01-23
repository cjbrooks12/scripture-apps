package com.caseyjbrooks.prayer.repository

import com.caseyjbrooks.prayer.models.PrayerUser
import com.caseyjbrooks.prayer.repository.user.FakePrayerUserRepository
import com.caseyjbrooks.prayer.repository.user.PrayerUserRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

public class FakePrayerUserRepositoryTest : StringSpec({
    "test" {
        val repository: PrayerUserRepository = FakePrayerUserRepository(
            PrayerUser("one", PrayerUser.SubscriptionStatus.Free),
        )

        val responseList = repository.getUserProfile()

        responseList shouldBe PrayerUser("one", PrayerUser.SubscriptionStatus.Free)
    }
})
