package com.caseyjbrooks.prayer.domain

import com.caseyjbrooks.prayer.models.PrayerUser
import com.caseyjbrooks.prayer.repository.config.InMemoryPrayerConfig
import com.caseyjbrooks.prayer.utils.getCreatePrayerUseCase
import com.caseyjbrooks.prayer.utils.getPrayer
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.StringSpec

public class CreatePrayerUseCaseTest : StringSpec({
    val config = InMemoryPrayerConfig()

    "logged out > fails" {
        val useCase = getCreatePrayerUseCase(null)

        shouldThrowAny {
            useCase(getPrayer("1", "prayer one"))
        }
    }

    "logged in > free plan > 0 saved prayers > succeeds" {
        val useCase = getCreatePrayerUseCase(
            PrayerUser("User", PrayerUser.SubscriptionStatus.Free),
        )

        // we can add the max number of prayers just fine
        shouldNotThrowAny {
            repeat(config.maxPrayersOnFreePlan) { index ->
                useCase(getPrayer("$index", "prayer $index"))
            }
        }
    }

    "logged in > free plan > saved prayers at limit > fails" {
        val useCase = getCreatePrayerUseCase(
            PrayerUser("User", PrayerUser.SubscriptionStatus.Free),
        )

        // we can add the max number of prayers just fine
        shouldNotThrowAny {
            repeat(config.maxPrayersOnFreePlan) { index ->
                useCase(getPrayer("$index", "prayer $index"))
            }
        }

        // once we've hit that limit, subsequent additions will fail
        shouldThrowAny {
            useCase(getPrayer("extra prayer", "extra prayer"))
        }
    }

    "logged in > subscriber > 0 saved prayers > succeeds" {
        val useCase = getCreatePrayerUseCase(
            PrayerUser("User", PrayerUser.SubscriptionStatus.Subscribed),
        )

        // we can add the max number of prayers just fine
        shouldNotThrowAny {
            repeat(config.maxPrayersOnFreePlan) { index ->
                useCase(getPrayer("$index", "prayer $index"))
            }
        }
    }

    "logged in > subscriber > saved prayers at limit > fails" {
        val useCase = getCreatePrayerUseCase(
            PrayerUser("User", PrayerUser.SubscriptionStatus.Subscribed),
        )

        // we can add the max number of prayers just fine
        shouldNotThrowAny {
            repeat(config.maxPrayersOnFreePlan) { index ->
                useCase(getPrayer("$index", "prayer $index"))
            }
        }

        // once we've hit that limit, subsequent additions will fail
        shouldNotThrowAny {
            useCase(getPrayer("extra prayer", "extra prayer"))
        }
    }
})
