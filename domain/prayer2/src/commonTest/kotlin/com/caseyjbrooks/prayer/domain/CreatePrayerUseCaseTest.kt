package com.caseyjbrooks.prayer.domain

import com.caseyjbrooks.prayer.domain.create.CreatePrayerUseCase
import com.caseyjbrooks.prayer.models.PrayerId
import com.caseyjbrooks.prayer.models.PrayerUser
import com.caseyjbrooks.prayer.models.SavedPrayer
import com.caseyjbrooks.prayer.models.SavedPrayerType
import com.caseyjbrooks.prayer.repository.config.PrayerConfig
import com.caseyjbrooks.prayer.utils.koinTest
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.StringSpec
import kotlinx.datetime.Instant

public class CreatePrayerUseCaseTest : StringSpec({
    fun getPrayer(millis: Long, id: String, text: String): SavedPrayer {
        val instant = Instant.fromEpochMilliseconds(millis)
        return SavedPrayer(
            uuid = PrayerId(id),
            text = text,
            prayerType = SavedPrayerType.Persistent,
            tags = listOf(),
            archived = false,
            archivedAt = null,
            createdAt = instant,
            updatedAt = instant,
        )
    }

    "logged out > fails" {
        koinTest(prayerUser = null) {
            val useCase: CreatePrayerUseCase = get()

            shouldThrowAny {
                useCase(getPrayer(0L, "1", "prayer one"))
            }
        }
    }

    "logged in > free plan > 0 saved prayers > succeeds" {
        koinTest(prayerUser = PrayerUser("User", PrayerUser.SubscriptionStatus.Free)) {
            val useCase: CreatePrayerUseCase = get()
            val config: PrayerConfig = get()

            // we can add the max number of prayers just fine
            shouldNotThrowAny {
                repeat(config.maxPrayersOnFreePlan) { index ->
                    useCase(getPrayer(0L, "$index", "prayer $index"))
                }
            }
        }
    }

    "logged in > free plan > saved prayers at limit > fails" {
        koinTest(prayerUser = PrayerUser("User", PrayerUser.SubscriptionStatus.Free)) {
            val useCase: CreatePrayerUseCase = get()
            val config: PrayerConfig = get()

            // we can add the max number of prayers just fine
            shouldNotThrowAny {
                repeat(config.maxPrayersOnFreePlan) { index ->
                    useCase(getPrayer(0L, "$index", "prayer $index"))
                }
            }

            // once we've hit that limit, subsequent additions will fail
            shouldThrowAny {
                useCase(getPrayer(0L, "extra prayer", "extra prayer"))
            }
        }
    }

    "logged in > subscriber > 0 saved prayers > succeeds" {
        koinTest(prayerUser = PrayerUser("User", PrayerUser.SubscriptionStatus.Subscribed)) {
            val useCase: CreatePrayerUseCase = get()
            val config: PrayerConfig = get()

            // we can add the max number of prayers just fine
            shouldNotThrowAny {
                repeat(config.maxPrayersOnFreePlan) { index ->
                    useCase(getPrayer(0L, "$index", "prayer $index"))
                }
            }
        }
    }

    "logged in > subscriber > saved prayers at limit > fails" {
        koinTest(prayerUser = PrayerUser("User", PrayerUser.SubscriptionStatus.Subscribed)) {
            val useCase: CreatePrayerUseCase = get()
            val config: PrayerConfig = get()

            // we can add the max number of prayers just fine
            shouldNotThrowAny {
                repeat(config.maxPrayersOnFreePlan) { index ->
                    useCase(getPrayer(0L, "$index", "prayer $index"))
                }
            }

            // once we've hit that limit, subsequent additions will fail
            shouldNotThrowAny {
                useCase(getPrayer(0L, "extra prayer", "extra prayer"))
            }
        }
    }
})
