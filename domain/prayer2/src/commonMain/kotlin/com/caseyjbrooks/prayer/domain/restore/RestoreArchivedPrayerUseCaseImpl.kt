package com.caseyjbrooks.prayer.domain.restore

import com.caseyjbrooks.prayer.domain.update.UpdatePrayerUseCase
import com.caseyjbrooks.prayer.models.SavedPrayer
import com.caseyjbrooks.prayer.repository.saved.SavedPrayersRepository
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock

internal class RestoreArchivedPrayerUseCaseImpl(
    private val savedPrayersRepository: SavedPrayersRepository,
    private val updatePrayerUseCase: UpdatePrayerUseCase,
    private val clock: Clock,
) : RestoreArchivedPrayerUseCase {
    override suspend operator fun invoke(prayer: SavedPrayer) {
        val latestPrayer = savedPrayersRepository.getPrayerById(prayer.uuid).first()
            ?: error("Prayer with ID ${prayer.uuid} does not exist")

        if (!latestPrayer.archived) {
            // no-op, not in the archives
        } else {
            updatePrayerUseCase(
                prayer.copy(
                    archived = false,
                    archivedAt = null,
                ),
            )
        }
    }
}
