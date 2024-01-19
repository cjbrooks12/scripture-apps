package com.caseyjbrooks.prayer.repository

import com.caseyjbrooks.prayer.repository.config.FakePrayerConfig
import com.caseyjbrooks.prayer.repository.config.PrayerConfig
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

public class FakePrayerConfigTest : StringSpec({
    "test" {
        val repository: PrayerConfig = FakePrayerConfig()
        repository.maxPrayersOnFreePlan shouldBe 10
    }
})
