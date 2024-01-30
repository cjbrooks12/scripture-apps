package com.caseyjbrooks.prayer.domain.createFromText

import com.caseyjbrooks.database.UuidFactory
import com.caseyjbrooks.prayer.domain.create.CreatePrayerUseCase
import com.caseyjbrooks.prayer.models.PrayerId
import com.caseyjbrooks.prayer.models.SavedPrayer
import com.caseyjbrooks.prayer.models.SavedPrayerType
import kotlinx.datetime.Clock

internal class CreatePrayerFromTextUseCaseImpl(
    private val createPrayerUseCase: CreatePrayerUseCase,
    private val uuidFactory: UuidFactory,
    private val clock: Clock,
) : CreatePrayerFromTextUseCase {
    override suspend operator fun invoke(text: String): SavedPrayer {
        val newPrayer = SavedPrayer(
            uuid = PrayerId(uuidFactory.getNewUuid()),
            text = text,
            prayerType = SavedPrayerType.Persistent,
            tags = buildList {
            },
            archived = false,
            archivedAt = null,
            createdAt = clock.now(),
            updatedAt = clock.now(),
        )
        return createPrayerUseCase(newPrayer)
    }
}
