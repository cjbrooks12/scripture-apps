package com.caseyjbrooks.scripturenow.viewmodel

import com.caseyjbrooks.scripturenow.viewmodel.home.HomeViewModelProvider
import com.caseyjbrooks.scripturenow.viewmodel.memory.detail.MemoryVerseDetailsViewModelProvider
import com.caseyjbrooks.scripturenow.viewmodel.memory.edit.CreateOrEditMemoryVerseViewModelProvider
import com.caseyjbrooks.scripturenow.viewmodel.memory.list.MemoryVerseListViewModelProvider
import com.caseyjbrooks.scripturenow.viewmodel.prayer.detail.PrayerDetailsViewModelProvider
import com.caseyjbrooks.scripturenow.viewmodel.prayer.edit.CreateOrEditPrayerViewModelProvider
import com.caseyjbrooks.scripturenow.viewmodel.prayer.list.PrayerListViewModelProvider
import com.caseyjbrooks.scripturenow.viewmodel.settings.SettingsViewModelProvider
import com.caseyjbrooks.scripturenow.viewmodel.votd.VerseOfTheDayViewModelProvider

public interface ViewModelsInjector :
    HomeViewModelProvider,
    MemoryVerseDetailsViewModelProvider,
    CreateOrEditMemoryVerseViewModelProvider,
    MemoryVerseListViewModelProvider,
    PrayerDetailsViewModelProvider,
    CreateOrEditPrayerViewModelProvider,
    PrayerListViewModelProvider,
    SettingsViewModelProvider,
    VerseOfTheDayViewModelProvider
