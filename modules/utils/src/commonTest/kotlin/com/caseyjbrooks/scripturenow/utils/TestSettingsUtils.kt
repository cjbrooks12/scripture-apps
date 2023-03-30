package com.caseyjbrooks.scripturenow.utils

import com.russhwolf.settings.MapSettings
import com.russhwolf.settings.ObservableSettings
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first

private enum class TestEnum {
    One, Two, Three
}

private class TestSettings(
    private val settings: ObservableSettings
) {
    var enumValue = EnumSettingsProperty(settings, "enumValue", TestEnum.One) { TestEnum.valueOf(it) }
    var intValue = IntSettingsProperty(settings, "intValue")
    var longValue = LongSettingsProperty(settings, "longValue")
    var floatValue = FloatSettingsProperty(settings, "floatValue")
    var doubleValue = DoubleSettingsProperty(settings, "doubleValue")
    var stringValue = StringSettingsProperty(settings, "stringValue")
    var booleanValue = BooleanSettingsProperty(settings, "booleanValue")
}

private suspend fun <T> SettingsProperty<T>.await(): T {
    return this.getAsFlow().first()
}

public class TestSettingsUtils : StringSpec({
    "test enumSettingOf" {
        val backingMap: MutableMap<String, Any> = mutableMapOf()
        val testSettings = TestSettings(MapSettings(backingMap))
        testSettings.enumValue.await() shouldBe TestEnum.One
        backingMap["enumValue"] shouldBe null

        testSettings.enumValue.set(TestEnum.Two)
        testSettings.enumValue.await() shouldBe TestEnum.Two
        backingMap["enumValue"] shouldBe "Two"
    }

    "test intSetting" {
        val backingMap: MutableMap<String, Any> = mutableMapOf()
        val testSettings = TestSettings(MapSettings(backingMap))
        testSettings.intValue.await() shouldBe 0
        backingMap["intValue"] shouldBe null

        testSettings.intValue.set(1)
        testSettings.intValue.await() shouldBe 1
        backingMap["intValue"] shouldBe "1"

        backingMap["intValue"] = "2"
        testSettings.intValue.await() shouldBe 2
        backingMap["intValue"] shouldBe "2"

        testSettings.intValue.set(3)
        testSettings.intValue.await() shouldBe 3
        backingMap["intValue"] shouldBe "3"
    }

    "test longSetting" {
        val backingMap: MutableMap<String, Any> = mutableMapOf()
        val testSettings = TestSettings(MapSettings(backingMap))
        testSettings.longValue.await() shouldBe 0
        backingMap["longValue"] shouldBe null

        testSettings.longValue.set(1L)
        testSettings.longValue.await() shouldBe 1
        backingMap["longValue"] shouldBe "1"

        backingMap["longValue"] = "2"
        testSettings.longValue.await() shouldBe 2
        backingMap["longValue"] shouldBe "2"

        testSettings.longValue.set(3L)
        testSettings.longValue.await() shouldBe 3
        backingMap["longValue"] shouldBe "3"
    }

    "test doubleSetting" {
        val backingMap: MutableMap<String, Any> = mutableMapOf()
        val testSettings = TestSettings(MapSettings(backingMap))
        testSettings.doubleValue.await() shouldBe 0
        backingMap["doubleValue"] shouldBe null

        testSettings.doubleValue.set(1.0)
        testSettings.doubleValue.await() shouldBe 1
        backingMap["doubleValue"] shouldBe "1.0"

        backingMap["doubleValue"] = "2.2"
        testSettings.doubleValue.await() shouldBe 2.2
        backingMap["doubleValue"] shouldBe "2.2"

        testSettings.doubleValue.set(3.3)
        testSettings.doubleValue.await() shouldBe 3.3
        backingMap["doubleValue"] shouldBe "3.3"
    }

    "test floatSetting" {
        val backingMap: MutableMap<String, Any> = mutableMapOf()
        val testSettings = TestSettings(MapSettings(backingMap))
        testSettings.floatValue.await() shouldBe 0
        backingMap["floatValue"] shouldBe null

        testSettings.floatValue.set(1f)
        testSettings.floatValue.await() shouldBe 1f
        backingMap["floatValue"] shouldBe "1.0"

        backingMap["floatValue"] = "2.2"
        testSettings.floatValue.await() shouldBe 2.2f
        backingMap["floatValue"] shouldBe "2.2"

        testSettings.floatValue.set(3.3f)
        testSettings.floatValue.await() shouldBe 3.3f
        backingMap["floatValue"] shouldBe "3.3"
    }

    "test booleanSetting" {
        val backingMap: MutableMap<String, Any> = mutableMapOf()
        val testSettings = TestSettings(MapSettings(backingMap))
        testSettings.booleanValue.await() shouldBe false
        backingMap["booleanValue"] shouldBe null

        testSettings.booleanValue.set(true)
        testSettings.booleanValue.await() shouldBe true
        backingMap["booleanValue"] shouldBe "true"

        backingMap["booleanValue"] = "false"
        testSettings.booleanValue.await() shouldBe false
        backingMap["booleanValue"] shouldBe "false"

        testSettings.booleanValue.set(true)
        testSettings.booleanValue.await() shouldBe true
        backingMap["booleanValue"] shouldBe "true"
    }
})
