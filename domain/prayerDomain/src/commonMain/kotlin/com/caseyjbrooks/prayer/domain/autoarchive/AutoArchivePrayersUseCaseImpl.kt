package com.caseyjbrooks.prayer.domain.autoarchive

import com.caseyjbrooks.prayer.domain.archive.ArchivePrayerUseCase
import com.caseyjbrooks.prayer.models.ArchiveStatus
import com.caseyjbrooks.prayer.models.SavedPrayerType
import com.caseyjbrooks.prayer.repository.saved.SavedPrayersRepository
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock

internal class AutoArchivePrayersUseCaseImpl(
    private val savedPrayersRepository: SavedPrayersRepository,
    private val archivePrayerUseCase: ArchivePrayerUseCase,
    private val clock: Clock,
) : AutoArchivePrayersUseCase {
    override suspend operator fun invoke() {
        val currentInstant = clock.now()
        val prayersToArchive = savedPrayersRepository
            .getPrayers(
                ArchiveStatus.NotArchived,
                setOf(
                    SavedPrayerType.ScheduledCompletable(
                        clock.now(),
                    ),
                ),
                emptySet(),
            )
            .first()
            .filter { prayer ->
                check(prayer.prayerType is SavedPrayerType.ScheduledCompletable)
                currentInstant > (prayer.prayerType as SavedPrayerType.ScheduledCompletable).completionDate
            }

        prayersToArchive.forEach { prayer ->
            archivePrayerUseCase(prayer)
        }
    }
}
