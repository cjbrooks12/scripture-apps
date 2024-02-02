package com.caseyjbrooks.verses.domain

import com.caseyjbrooks.verses.domain.archive.ArchiveVerseUseCase
import com.caseyjbrooks.verses.domain.archive.ArchiveVerseUseCaseImpl
import com.caseyjbrooks.verses.domain.create.CreateVerseUseCase
import com.caseyjbrooks.verses.domain.create.CreateVerseUseCaseImpl
import com.caseyjbrooks.verses.domain.createFromText.CreateVerseFromTextUseCase
import com.caseyjbrooks.verses.domain.createFromText.CreateVerseFromTextUseCaseImpl
import com.caseyjbrooks.verses.domain.getbyid.GetVerseByIdUseCase
import com.caseyjbrooks.verses.domain.getbyid.GetVerseByIdUseCaseImpl
import com.caseyjbrooks.verses.domain.query.QueryVersesUseCase
import com.caseyjbrooks.verses.domain.query.QueryVersesUseCaseImpl
import com.caseyjbrooks.verses.domain.restore.RestoreArchivedVerseUseCase
import com.caseyjbrooks.verses.domain.restore.RestoreArchivedVerseUseCaseImpl
import com.caseyjbrooks.verses.domain.update.UpdateVerseUseCase
import com.caseyjbrooks.verses.domain.update.UpdateVerseUseCaseImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

public val verseDomainModule: Module = module {
    factoryOf(::ArchiveVerseUseCaseImpl).bind<ArchiveVerseUseCase>()
    factoryOf(::CreateVerseUseCaseImpl).bind<CreateVerseUseCase>()
    factoryOf(::CreateVerseFromTextUseCaseImpl).bind<CreateVerseFromTextUseCase>()
    factoryOf(::GetVerseByIdUseCaseImpl).bind<GetVerseByIdUseCase>()
    factoryOf(::QueryVersesUseCaseImpl).bind<QueryVersesUseCase>()
    factoryOf(::RestoreArchivedVerseUseCaseImpl).bind<RestoreArchivedVerseUseCase>()
    factoryOf(::UpdateVerseUseCaseImpl).bind<UpdateVerseUseCase>()
}
