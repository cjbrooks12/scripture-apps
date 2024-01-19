package com.caseyjbrooks.prayer.domain.update

import com.caseyjbrooks.prayer.models.SavedPrayer
import com.caseyjbrooks.prayer.repository.saved.SavedPrayersRepository
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock

internal class UpdatePrayerUseCaseImpl(
    private val savedPrayersRepository: SavedPrayersRepository,
    private val clock: Clock,
) : UpdatePrayerUseCase {
    override suspend operator fun invoke(prayer: SavedPrayer): SavedPrayer {
        val latestPrayer = savedPrayersRepository.getPrayerById(prayer.uuid).first()
            ?: error("Prayer with ID ${prayer.uuid} does not exist")

        return if (latestPrayer == prayer) {
            // no-op, there are no changes needed
            prayer
        } else {
            val updatedPrayer = prayer.copy(
                updatedAt = clock.now(),
            )
            savedPrayersRepository.updatePrayer(updatedPrayer)
            updatedPrayer
        }
    }
}
