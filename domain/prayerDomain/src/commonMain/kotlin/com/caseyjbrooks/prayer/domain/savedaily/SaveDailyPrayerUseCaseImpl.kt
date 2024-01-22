package com.caseyjbrooks.prayer.domain.savedaily

import com.caseyjbrooks.database.UuidFactory
import com.caseyjbrooks.prayer.domain.create.CreatePrayerUseCase
import com.caseyjbrooks.prayer.models.DailyPrayer
import com.caseyjbrooks.prayer.models.PrayerId
import com.caseyjbrooks.prayer.models.SavedPrayer
import com.caseyjbrooks.prayer.models.SavedPrayerType
import com.caseyjbrooks.prayer.repository.daily.DailyPrayerRepository
import com.caseyjbrooks.prayer.repository.saved.SavedPrayersRepository
import com.caseyjbrooks.prayer.repository.settings.PrayerUserSettings
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock

internal class SaveDailyPrayerUseCaseImpl(
    private val savedPrayersRepository: SavedPrayersRepository,
    private val dailyPrayerRepository: DailyPrayerRepository,
    private val prayerUserSettings: PrayerUserSettings,
    private val createPrayerUseCase: CreatePrayerUseCase,
    private val uuidFactory: UuidFactory,
    private val clock: Clock,
) : SaveDailyPrayerUseCase {

    override suspend operator fun invoke(): SavedPrayer {
        // assumes success, throws and exception if we cannot get the daily prayer
        val dailyPrayer: DailyPrayer = dailyPrayerRepository
            .getTodaysDailyPrayer().first()
            ?: error("Error getting today's daily prayer.")

        // check to see if we've already saved this prayer for today
        val prayerByText: SavedPrayer? = savedPrayersRepository
            .getPrayerByText(dailyPrayer.text).first()

        return if (prayerByText == null) {
            val newSavedPrayer = SavedPrayer(
                uuid = PrayerId(uuidFactory.getNewUuid()),
                text = dailyPrayer.text,
                prayerType = SavedPrayerType.Persistent,
                tags = buildList {
                    if (prayerUserSettings.addDefaultTagToSavedDailyPrayer) {
                        this += prayerUserSettings.dailyVerseTag
                    }
                    if (prayerUserSettings.saveTagsFromDailyPrayer) {
                        this += dailyPrayer.tags
                    }
                },
                archived = false,
                archivedAt = null,
                createdAt = clock.now(),
                updatedAt = clock.now(),
            )
            createPrayerUseCase(newSavedPrayer)

            newSavedPrayer
        } else {
            prayerByText
        }
    }
}
