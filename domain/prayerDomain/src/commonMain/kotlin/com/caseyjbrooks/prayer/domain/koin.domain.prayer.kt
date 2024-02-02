package com.caseyjbrooks.prayer.domain

import com.caseyjbrooks.prayer.domain.archive.ArchivePrayerUseCase
import com.caseyjbrooks.prayer.domain.archive.ArchivePrayerUseCaseImpl
import com.caseyjbrooks.prayer.domain.autoarchive.AutoArchivePrayersUseCase
import com.caseyjbrooks.prayer.domain.autoarchive.AutoArchivePrayersUseCaseImpl
import com.caseyjbrooks.prayer.domain.create.CreatePrayerUseCase
import com.caseyjbrooks.prayer.domain.create.CreatePrayerUseCaseImpl
import com.caseyjbrooks.prayer.domain.createFromText.CreatePrayerFromTextUseCase
import com.caseyjbrooks.prayer.domain.createFromText.CreatePrayerFromTextUseCaseImpl
import com.caseyjbrooks.prayer.domain.getbyid.GetPrayerByIdUseCase
import com.caseyjbrooks.prayer.domain.getbyid.GetPrayerByIdUseCaseImpl
import com.caseyjbrooks.prayer.domain.getdaily.GetDailyPrayerUseCase
import com.caseyjbrooks.prayer.domain.getdaily.GetDailyPrayerUseCaseImpl
import com.caseyjbrooks.prayer.domain.query.QueryPrayersUseCase
import com.caseyjbrooks.prayer.domain.query.QueryPrayersUseCaseImpl
import com.caseyjbrooks.prayer.domain.restore.RestoreArchivedPrayerUseCase
import com.caseyjbrooks.prayer.domain.restore.RestoreArchivedPrayerUseCaseImpl
import com.caseyjbrooks.prayer.domain.savedaily.SaveDailyPrayerUseCase
import com.caseyjbrooks.prayer.domain.savedaily.SaveDailyPrayerUseCaseImpl
import com.caseyjbrooks.prayer.domain.update.UpdatePrayerUseCase
import com.caseyjbrooks.prayer.domain.update.UpdatePrayerUseCaseImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

public val prayerDomainModule: Module = module {
    factoryOf(::ArchivePrayerUseCaseImpl).bind<ArchivePrayerUseCase>()
    factoryOf(::AutoArchivePrayersUseCaseImpl).bind<AutoArchivePrayersUseCase>()
    factoryOf(::CreatePrayerUseCaseImpl).bind<CreatePrayerUseCase>()
    factoryOf(::CreatePrayerFromTextUseCaseImpl).bind<CreatePrayerFromTextUseCase>()
    factoryOf(::GetPrayerByIdUseCaseImpl).bind<GetPrayerByIdUseCase>()
    factoryOf(::GetDailyPrayerUseCaseImpl).bind<GetDailyPrayerUseCase>()
    factoryOf(::QueryPrayersUseCaseImpl).bind<QueryPrayersUseCase>()
    factoryOf(::RestoreArchivedPrayerUseCaseImpl).bind<RestoreArchivedPrayerUseCase>()
    factoryOf(::SaveDailyPrayerUseCaseImpl).bind<SaveDailyPrayerUseCase>()
    factoryOf(::UpdatePrayerUseCaseImpl).bind<UpdatePrayerUseCase>()
}
