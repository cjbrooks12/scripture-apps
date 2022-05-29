package com.copperleaf.beer.main

import com.copperleaf.ballast.savedstate.RestoreStateScope
import com.copperleaf.ballast.savedstate.SaveStateScope
import com.copperleaf.ballast.savedstate.SavedStateAdapter
import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.JvmPreferencesSettings
import com.russhwolf.settings.Settings
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSettingsImplementation::class, ExperimentalSerializationApi::class)
class MainSavedStateAdapter(
    private val prefs: MainSavedStateAdapter.Prefs = PrefsImpl()
) : SavedStateAdapter<
    MainContract.Inputs,
    MainContract.Events,
    MainContract.State> {

    interface Prefs {
        var state: MainContract.State
    }

    class PrefsImpl : Prefs {
        private val settings: Settings = JvmPreferencesSettings.Factory().create()
        private val json: Json = Json {
            prettyPrint = true
            explicitNulls = true
            encodeDefaults = true
        }
        private val stateKey = "mainState"

        override var state: MainContract.State
            get() {
                return settings
                    .getString(stateKey)
                    .let { json.decodeFromString(MainContract.State.serializer(), it) }
            }
            set(value) {
                return settings
                    .putString(stateKey, json.encodeToString(MainContract.State.serializer(), value))
            }
    }

    override suspend fun SaveStateScope<
        MainContract.Inputs,
        MainContract.Events,
        MainContract.State>.save() {
        saveAll { state ->
            prefs.state = state
        }
    }

    override suspend fun RestoreStateScope<
        MainContract.Inputs,
        MainContract.Events,
        MainContract.State>.restore(): MainContract.State {
        return prefs.state
    }
}
