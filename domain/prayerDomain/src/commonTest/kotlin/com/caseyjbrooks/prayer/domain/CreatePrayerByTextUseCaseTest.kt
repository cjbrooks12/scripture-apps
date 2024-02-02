package com.caseyjbrooks.prayer.domain

import com.caseyjbrooks.prayer.domain.create.CreatePrayerUseCase
import com.caseyjbrooks.prayer.models.PrayerNotification
import com.caseyjbrooks.prayer.models.SavedPrayer
import com.caseyjbrooks.prayer.models.SavedPrayerType
import com.caseyjbrooks.prayer.utils.PrayerId
import com.caseyjbrooks.prayer.utils.koinTest
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.StringSpec
import kotlinx.datetime.Instant

public class CreatePrayerByTextUseCaseTest : StringSpec({
    fun getPrayer(millis: Long, id: Int, text: String): SavedPrayer {
        val instant = Instant.fromEpochMilliseconds(millis)
        return SavedPrayer(
            uuid = PrayerId(id),
            text = text,
            prayerType = SavedPrayerType.Persistent,
            tags = listOf(),
            archived = false,
            archivedAt = null,
            notification = PrayerNotification.None,
            createdAt = instant,
            updatedAt = instant,
        )
    }

    "logged out > fails" {
        koinTest(prayerUser = null) {
            val useCase: CreatePrayerUseCase = get()

            shouldThrowAny {
                useCase(getPrayer(0L, 1, "prayer one"))
            }
        }
    }
})
