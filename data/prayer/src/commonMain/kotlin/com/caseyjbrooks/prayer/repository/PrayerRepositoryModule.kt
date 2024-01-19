package com.caseyjbrooks.prayer.repository

import com.caseyjbrooks.prayer.repository.config.BuildConfigPrayerConfig
import com.caseyjbrooks.prayer.repository.config.FakePrayerConfig
import com.caseyjbrooks.prayer.repository.config.PrayerConfig
import com.caseyjbrooks.prayer.repository.daily.DailyPrayerRepository
import com.caseyjbrooks.prayer.repository.daily.FakeDailyPrayerRepository
import com.caseyjbrooks.prayer.repository.saved.FakeSavedPrayersRepository
import com.caseyjbrooks.prayer.repository.saved.OfflineDatabaseSavedPrayersRepository
import com.caseyjbrooks.prayer.repository.saved.SavedPrayersRepository
import com.caseyjbrooks.prayer.repository.settings.FakePrayerUserSettings
import com.caseyjbrooks.prayer.repository.settings.KeyValuePrayerUserSettings
import com.caseyjbrooks.prayer.repository.settings.PrayerUserSettings
import com.caseyjbrooks.prayer.repository.user.FakePrayerUserRepository
import com.caseyjbrooks.prayer.repository.user.PrayerUserRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

public val realPrayerRepositoryModule: Module = module {
    singleOf(::BuildConfigPrayerConfig).bind<PrayerConfig>()
    singleOf(::FakeDailyPrayerRepository).bind<DailyPrayerRepository>()
    singleOf(::OfflineDatabaseSavedPrayersRepository).bind<SavedPrayersRepository>()
    singleOf(::KeyValuePrayerUserSettings).bind<PrayerUserSettings>()
    singleOf(::FakePrayerUserRepository).bind<PrayerUserRepository>()
}

public val fakePrayerRepositoryModule: Module = module {
    single<PrayerConfig> { FakePrayerConfig() }
    single<DailyPrayerRepository> { FakeDailyPrayerRepository(getOrNull()) }
    single<SavedPrayersRepository> { FakeSavedPrayersRepository() }
    single<PrayerUserSettings> { FakePrayerUserSettings() }
    single<PrayerUserRepository> { FakePrayerUserRepository(getOrNull()) }
}
