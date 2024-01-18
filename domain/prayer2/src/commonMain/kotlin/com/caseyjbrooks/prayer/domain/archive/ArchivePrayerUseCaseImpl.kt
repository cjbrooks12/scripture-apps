package com.caseyjbrooks.prayer.domain.archive

import com.caseyjbrooks.prayer.domain.update.UpdatePrayerUseCase
import com.caseyjbrooks.prayer.models.SavedPrayer
import com.caseyjbrooks.prayer.repository.saved.SavedPrayersRepository
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock

public class ArchivePrayerUseCaseImpl(
    private val savedPrayersRepository: SavedPrayersRepository,
    private val updatePrayerUseCase: UpdatePrayerUseCase,
    private val clock: Clock,
) : ArchivePrayerUseCase {
    override suspend operator fun invoke(prayer: SavedPrayer) {
        val latestPrayer = savedPrayersRepository.getPrayerById(prayer.uuid).first()
            ?: error("Prayer with ID ${prayer.uuid} does not exist")

        if (latestPrayer.archived) {
            // no-op, already in the archives
        } else {
            updatePrayerUseCase(
                prayer.copy(
                    archived = true,
                    archivedAt = clock.now(),
                ),
            )
        }
    }
}
