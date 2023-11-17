package com.caseyjbrooks.prayer.repository

import com.caseyjbrooks.prayer.repository.config.InMemoryPrayerConfig
import com.caseyjbrooks.prayer.repository.config.PrayerConfig
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

public class InMemoryPrayerConfigTest : StringSpec({
    "test" {
        val repository: PrayerConfig = InMemoryPrayerConfig()
        repository.maxPrayersOnFreePlan shouldBe 10
    }
})
