package com.caseyjbrooks.scripturenow.utils

import com.russhwolf.settings.ObservableSettings
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

public abstract class SettingsProperty<T>(
    protected val settings: ObservableSettings,
    protected val propertyName: String,
    protected val defaultValue: T,
) {
    public abstract fun convertFromStorage(savedValue: String?): T?
    public abstract fun convertToStorage(value: T): String?

    public fun getAsFlow(): Flow<T> {
        return callbackFlow {
            val initialValue = settings.getStringOrNull(propertyName)
            send(convertFromStorage(initialValue) ?: defaultValue)

            val listener = settings.addStringOrNullListener(propertyName) { savedValue ->
                println("updating saved value: $propertyName=$savedValue")
                trySend(convertFromStorage(savedValue) ?: defaultValue)
            }

            awaitClose {
                println("closing listener: $propertyName")
                listener.deactivate()
            }
        }
    }

    public suspend fun set(value: T) {
        settings.putString(propertyName, convertToStorage(value) ?: "")
    }
}

public class EnumSettingsProperty<T : Enum<T>>(
    settings: ObservableSettings,
    propertyName: String,
    defaultValue: T,
    private val parse: (String) -> T?
) : SettingsProperty<T>(settings, propertyName, defaultValue) {
    override fun convertFromStorage(savedValue: String?): T? = savedValue?.let(parse) ?: defaultValue
    override fun convertToStorage(value: T): String? = value.name
}

public class IntSettingsProperty(
    settings: ObservableSettings,
    propertyName: String,
) : SettingsProperty<Int>(settings, propertyName, 0) {
    override fun convertFromStorage(savedValue: String?): Int? = savedValue?.toIntOrNull()
    override fun convertToStorage(value: Int): String? = value.toString()
}

public class BooleanSettingsProperty(
    settings: ObservableSettings,
    propertyName: String,
) : SettingsProperty<Boolean>(settings, propertyName, false) {
    override fun convertFromStorage(savedValue: String?): Boolean? = savedValue?.toBooleanStrictOrNull()
    override fun convertToStorage(value: Boolean): String? = value.toString()
}

public class StringSettingsProperty(
    settings: ObservableSettings,
    propertyName: String,
) : SettingsProperty<String>(settings, propertyName, "") {
    override fun convertFromStorage(savedValue: String?): String? = savedValue
    override fun convertToStorage(value: String): String? = value
}
