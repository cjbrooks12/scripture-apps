package com.caseyjbrooks.prayer.domain.create

import com.caseyjbrooks.prayer.models.ArchiveStatus
import com.caseyjbrooks.prayer.models.PrayerUser
import com.caseyjbrooks.prayer.models.SavedPrayer
import com.caseyjbrooks.prayer.repository.config.PrayerConfig
import com.caseyjbrooks.prayer.repository.saved.SavedPrayersRepository
import com.caseyjbrooks.prayer.repository.user.PrayerUserRepository
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock

public class CreatePrayerUseCaseImpl(
    private val savedPrayersRepository: SavedPrayersRepository,
    private val prayerConfig: PrayerConfig,
    private val prayerUserRepository: PrayerUserRepository,
    private val clock: Clock,
) : CreatePrayerUseCase {
    override suspend operator fun invoke(prayer: SavedPrayer): SavedPrayer {
        val prayerUser = prayerUserRepository.getUserProfile()
            ?: error("User not logged in")

        if (prayerUser.subscription == PrayerUser.SubscriptionStatus.Free) {
            // on the free plan, check that the user has not exceeded the threshold

            if (savedPrayersRepository
                    .getPrayers(ArchiveStatus.FullCollection, emptyList())
                    .first().size >= prayerConfig.maxPrayersOnFreePlan
            ) {
                error("Exceeded free plan limit")
            }
        }

        val currentInstant = clock.now()
        val updatedPrayer = prayer.copy(
            createdAt = currentInstant,
            updatedAt = currentInstant,
        )
        savedPrayersRepository.createPrayer(updatedPrayer)
        return updatedPrayer
    }
}
