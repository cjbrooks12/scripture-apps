package com.caseyjbrooks.prayer.domain.createFromText

import com.caseyjbrooks.database.UuidFactory
import com.caseyjbrooks.prayer.domain.create.CreatePrayerUseCase
import com.caseyjbrooks.prayer.models.PrayerId
import com.caseyjbrooks.prayer.models.PrayerNotification
import com.caseyjbrooks.prayer.models.PrayerTag
import com.caseyjbrooks.prayer.models.SavedPrayer
import com.caseyjbrooks.prayer.models.SavedPrayerType
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

internal class CreatePrayerFromTextUseCaseImpl(
    private val createPrayerUseCase: CreatePrayerUseCase,
    private val uuidFactory: UuidFactory,
    private val clock: Clock,
) : CreatePrayerFromTextUseCase {
    override suspend fun invoke(text: String, completionDate: Instant?, tags: Set<String>): SavedPrayer {
        val newPrayer = SavedPrayer(
            uuid = PrayerId(uuidFactory.getNewUuid()),
            text = text,
            prayerType = completionDate
                ?.let { SavedPrayerType.ScheduledCompletable(it) }
                ?: SavedPrayerType.Persistent,
            tags = tags.map { PrayerTag(it) },
            archived = false,
            archivedAt = null,
            notification = PrayerNotification.None,
            createdAt = clock.now(),
            updatedAt = clock.now(),
        )
        return createPrayerUseCase(newPrayer)
    }
}
