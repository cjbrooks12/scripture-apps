package com.caseyjbrooks.scripturenow.db.preferences

import com.russhwolf.settings.Settings

public object AppPreferencesProvider {
    public fun get(
        settings: Settings,
    ): AppPreferences {
        return AppPreferencesImpl(
            settings = settings
        )
    }
}
