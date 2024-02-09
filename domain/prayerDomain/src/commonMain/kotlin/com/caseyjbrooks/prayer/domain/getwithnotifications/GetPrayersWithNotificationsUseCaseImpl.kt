package com.caseyjbrooks.prayer.domain.getwithnotifications

import com.caseyjbrooks.prayer.models.ArchiveStatus
import com.caseyjbrooks.prayer.models.SavedPrayer
import com.caseyjbrooks.prayer.repository.saved.SavedPrayersRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onEmpty

/**
 * Retrieve the list 0f prayers which are configured to send notifications, and which are not archived. These prayers
 * will be scheduled to display their notifications.
 */
internal class GetPrayersWithNotificationsUseCaseImpl(
    private val repository: SavedPrayersRepository,
) : GetPrayersWithNotificationsUseCase {
    override suspend operator fun invoke(): List<SavedPrayer> {
        return repository
            .getPrayers(ArchiveStatus.NotArchived, emptySet(), emptySet(), true)
            .onEmpty { emptyList<SavedPrayer>() }
            .catch { emptyList<SavedPrayer>() }
            .firstOrNull()
            ?: emptyList()
    }
}
