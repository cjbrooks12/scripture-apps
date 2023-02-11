package com.caseyjbrooks.scripturenow.models

public object AppFeatures {
    public enum class App {
        ScriptureNow,
        TopicalBible,
        PrayerJournal,
    }

    public enum class Environment {
        Dev,
        Prod,
    }

    public enum class BuildType {
        Debug,
        Release,
    }
}
