package com.caseyjbrooks.prayer.domain.autoarchive

import com.caseyjbrooks.prayer.models.SavedPrayerType

/**
 * On a regular schedule, automatically move prayers of type [SavedPrayerType.ScheduledCompletable] to the archives if
 * if it past its scheduled time.
 */
public typealias AutoArchivePrayersUseCase = suspend () -> Unit
