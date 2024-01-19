package com.caseyjbrooks.prayer.repository

import com.caseyjbrooks.prayer.models.PrayerTag
import com.caseyjbrooks.prayer.repository.settings.FakePrayerUserSettings
import com.caseyjbrooks.prayer.repository.settings.PrayerUserSettings
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

public class FakePrayerUserSettingsTest : StringSpec({
    "test" {
        val repository: PrayerUserSettings = FakePrayerUserSettings()

        // check original values
        repository.dailyVerseTag shouldBe PrayerTag("Daily Prayer")
        repository.saveTagsFromDailyPrayer shouldBe true

        // update values
        repository.dailyVerseTag = PrayerTag("My Daily Prayers")
        repository.saveTagsFromDailyPrayer = false

        // ensure the updated values were saved
        repository.dailyVerseTag shouldBe PrayerTag("My Daily Prayers")
        repository.saveTagsFromDailyPrayer shouldBe false
    }
})
