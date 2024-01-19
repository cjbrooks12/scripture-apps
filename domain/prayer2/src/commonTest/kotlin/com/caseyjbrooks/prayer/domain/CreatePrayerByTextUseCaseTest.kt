package com.caseyjbrooks.prayer.domain

import com.caseyjbrooks.prayer.repository.config.FakePrayerConfig
import com.caseyjbrooks.prayer.utils.getCreatePrayerUseCase
import com.caseyjbrooks.prayer.utils.getPrayer
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.StringSpec

public class CreatePrayerByTextUseCaseTest : StringSpec({
    val config = FakePrayerConfig()

    "logged out > fails" {
        val useCase = getCreatePrayerUseCase(null)

        shouldThrowAny {
            useCase(getPrayer("1", "prayer one"))
        }
    }
})
