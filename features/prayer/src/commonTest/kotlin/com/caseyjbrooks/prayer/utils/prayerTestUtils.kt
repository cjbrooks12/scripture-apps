package com.caseyjbrooks.prayer.utils

import com.caseyjbrooks.prayer.domain.create.CreatePrayerUseCase
import com.caseyjbrooks.prayer.domain.create.CreatePrayerUseCaseImpl
import com.caseyjbrooks.prayer.domain.update.UpdatePrayerUseCase
import com.caseyjbrooks.prayer.domain.update.UpdatePrayerUseCaseImpl
import com.caseyjbrooks.prayer.models.PrayerId
import com.caseyjbrooks.prayer.models.PrayerTag
import com.caseyjbrooks.prayer.models.PrayerUser
import com.caseyjbrooks.prayer.models.SavedPrayer
import com.caseyjbrooks.prayer.models.SavedPrayerType
import com.caseyjbrooks.prayer.repository.config.InMemoryPrayerConfig
import com.caseyjbrooks.prayer.repository.saved.InMemorySavedPrayersRepository
import com.caseyjbrooks.prayer.repository.user.InMemoryPrayerUserRepository
import kotlinx.datetime.Clock

fun getPrayer(id: String, text: String, clock: Clock = TestClock()): SavedPrayer {
    val instant = clock.now()
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

fun getPrayer(id: String, archived: Boolean, clock: Clock, vararg tags: String): SavedPrayer {
    val instant = clock.now()
    return SavedPrayer(
        uuid = PrayerId(id),
        text = id,
        prayerType = SavedPrayerType.Persistent,
        tags = tags.map { PrayerTag(it) },
        archived = archived,
        archivedAt = if (archived) instant else null,
        createdAt = instant,
        updatedAt = instant,
    )
}

fun getPrayer(id: String, archived: Boolean, vararg tags: String): SavedPrayer {
    val instant = TestClock().now()
    return SavedPrayer(
        uuid = PrayerId(id),
        text = id,
        prayerType = SavedPrayerType.Persistent,
        tags = tags.map { PrayerTag(it) },
        archived = archived,
        archivedAt = if (archived) instant else null,
        createdAt = instant,
        updatedAt = instant,
    )
}

fun getCreatePrayerUseCase(prayerUser: PrayerUser?): CreatePrayerUseCase {
    val config = InMemoryPrayerConfig()
    return CreatePrayerUseCaseImpl(
        savedPrayersRepository = InMemorySavedPrayersRepository(),
        prayerConfig = config,
        prayerUserRepository = InMemoryPrayerUserRepository(prayerUser),
        clock = TestClock(),
    )
}

fun getUpdatePrayerUseCase(): UpdatePrayerUseCase {
    return UpdatePrayerUseCaseImpl(
        savedPrayersRepository = InMemorySavedPrayersRepository(),
        clock = TestClock(),
    )
}
