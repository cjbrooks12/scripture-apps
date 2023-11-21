package com.caseyjbrooks.prayer.domain.autoarchive

import com.caseyjbrooks.prayer.domain.archive.ArchivePrayerUseCase
import com.caseyjbrooks.prayer.models.ArchiveStatus
import com.caseyjbrooks.prayer.models.SavedPrayerType
import com.caseyjbrooks.prayer.repository.saved.SavedPrayersRepository
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

/**
 * On a regular schedule, automatically move prayers of type [SavedPrayerType.ScheduledCompletable] to the archives if
 * it past its scheduled time.
 */
public class AutoArchivePrayersUseCaseImpl(
    private val savedPrayersRepository: SavedPrayersRepository,
    private val archivePrayerUseCase: ArchivePrayerUseCase,
    private val clock: Clock,
    private val timeZone: TimeZone,
) : AutoArchivePrayersUseCase {
    override suspend operator fun invoke() {
        val currentInstant = clock.now()
        val prayersToArchive = savedPrayersRepository
            .getPrayers(
                ArchiveStatus.NotArchived,
                setOf(
                    SavedPrayerType.ScheduledCompletable(
                        clock.now().toLocalDateTime(timeZone),
                    ),
                ),
                emptySet(),
            )
            .first()
            .filter { prayer ->
                check(prayer.prayerType is SavedPrayerType.ScheduledCompletable)
                currentInstant > prayer.prayerType.completionDate.toInstant(timeZone)
            }

        prayersToArchive.forEach { prayer ->
            archivePrayerUseCase(prayer)
        }
    }
}
