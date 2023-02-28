package com.caseyjbrooks.scripturenow.db.preferences

import com.russhwolf.settings.ObservableSettings

public object AppPreferencesProvider {
    public fun get(
        settings: ObservableSettings,
    ): ObservableSettingsAppPreferences {
        return ObservableSettingsAppPreferences(
            settings = settings
        )
    }
}
