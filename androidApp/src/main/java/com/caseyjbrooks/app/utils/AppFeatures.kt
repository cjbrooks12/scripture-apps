package com.caseyjbrooks.app.utils

object AppFeatures {
    enum class App {
        ScriptureNow,
        TopicalBible,
        PrayerJournal,
    }

    enum class Environment {
        Dev,
        Prod,
    }

    enum class BuildType {
        Debug,
        Release,
    }
}
