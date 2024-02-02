package com.caseyjbrooks.verses

import com.caseyjbrooks.verses.models.VerseUser
import com.caseyjbrooks.verses.repository.config.BuildConfigVerseConfig
import com.caseyjbrooks.verses.repository.config.FakeVerseConfig
import com.caseyjbrooks.verses.repository.config.VerseConfig
import com.caseyjbrooks.verses.repository.saved.FakeSavedVersesRepository
import com.caseyjbrooks.verses.repository.saved.OfflineDatabaseSavedVersesRepository
import com.caseyjbrooks.verses.repository.saved.SavedVersesRepository
import com.caseyjbrooks.verses.repository.settings.FakeVerseUserSettings
import com.caseyjbrooks.verses.repository.settings.KeyValueVerseUserSettings
import com.caseyjbrooks.verses.repository.settings.VerseUserSettings
import com.caseyjbrooks.verses.repository.user.FakeVerseUserRepository
import com.caseyjbrooks.verses.repository.user.VerseUserRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

public val realVerseDataModule: Module = module {
    single<VerseUser?> { VerseUser("Casey", VerseUser.SubscriptionStatus.Free) }
    singleOf(::BuildConfigVerseConfig).bind<VerseConfig>()
    singleOf(::OfflineDatabaseSavedVersesRepository).bind<SavedVersesRepository>()
    singleOf(::KeyValueVerseUserSettings).bind<VerseUserSettings>()
    singleOf(::FakeVerseUserRepository).bind<VerseUserRepository>()
}

public val fakeVerseDataModule: Module = module {
    single<VerseUser?> { VerseUser("Casey", VerseUser.SubscriptionStatus.Free) }
    single<VerseConfig> { FakeVerseConfig() }
    single<SavedVersesRepository> { FakeSavedVersesRepository() }
    single<VerseUserSettings> { FakeVerseUserSettings() }
    single<VerseUserRepository> { FakeVerseUserRepository(getOrNull()) }
}
