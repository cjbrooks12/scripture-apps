package com.caseyjbrooks.scripturenow.utils

import com.russhwolf.settings.Settings
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

public fun <T : Enum<T>> Settings.enumSettingOf(
    defaultValue: T,
    parse: (String) -> T?
): ReadWriteProperty<Any, T> {
    val settings = this
    return object : ReadWriteProperty<Any, T> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T {
            val savedValue = settings.getStringOrNull(property.name)
            val parsedValue = savedValue?.let(parse)
            return parsedValue ?: defaultValue
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
            settings.putString(property.name, value.name)
        }
    }
}

public fun Settings.intSetting(
    defaultValue: Int = 0
): ReadWriteProperty<Any, Int> {
    val settings = this
    return object : ReadWriteProperty<Any, Int> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Int {
            return settings.getIntOrNull(property.name)
                ?: settings.getStringOrNull(property.name)?.toIntOrNull()
                ?: defaultValue
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
            settings.putInt(property.name, value)
        }
    }
}

public fun Settings.longSetting(
    defaultValue: Long = 0L
): ReadWriteProperty<Any, Long> {
    val settings = this
    return object : ReadWriteProperty<Any, Long> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Long {
            return settings.getLongOrNull(property.name)
                ?: settings.getStringOrNull(property.name)?.toLongOrNull()
                ?: defaultValue
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Long) {
            settings.putLong(property.name, value)
        }
    }
}

public fun Settings.floatSetting(
    defaultValue: Float = 0f
): ReadWriteProperty<Any, Float> {
    val settings = this
    return object : ReadWriteProperty<Any, Float> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Float {
            return settings.getFloatOrNull(property.name)
                ?: settings.getStringOrNull(property.name)?.toFloatOrNull()
                ?: defaultValue
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Float) {
            settings.putFloat(property.name, value)
        }
    }
}

public fun Settings.doubleSetting(
    defaultValue: Double = 0.0
): ReadWriteProperty<Any, Double> {
    val settings = this
    return object : ReadWriteProperty<Any, Double> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Double {
            return settings.getDoubleOrNull(property.name)
                ?: settings.getStringOrNull(property.name)?.toDoubleOrNull()
                ?: defaultValue
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Double) {
            settings.putDouble(property.name, value)
        }
    }
}

public fun Settings.stringSetting(
    defaultValue: String = ""
): ReadWriteProperty<Any, String> {
    val settings = this
    return object : ReadWriteProperty<Any, String> {
        override fun getValue(thisRef: Any, property: KProperty<*>): String {
            return settings.getStringOrNull(property.name)
                ?: defaultValue
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: String) {
            settings.putString(property.name, value)
        }
    }
}

public fun Settings.booleanSetting(
    defaultValue: Boolean = false
): ReadWriteProperty<Any, Boolean> {
    val settings = this
    return object : ReadWriteProperty<Any, Boolean> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
            return settings.getBooleanOrNull(property.name)
                ?: settings.getStringOrNull(property.name)?.toBooleanStrictOrNull()
                ?: defaultValue
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
            settings.putBoolean(property.name, value)
        }
    }
}
