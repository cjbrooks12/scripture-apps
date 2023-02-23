package com.caseyjbrooks.scripturenow.db.preferences

import com.russhwolf.settings.ObservableSettings

public object AppPreferencesProvider {
    public fun get(
        settings: ObservableSettings,
    ): AppPreferences {
        return AppPreferencesImpl(
            settings = settings
        )
    }
}
