package com.caseyjbrooks.scripturenow.utils

import com.russhwolf.settings.MapSettings
import com.russhwolf.settings.Settings
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

private enum class TestEnum {
    One, Two, Three
}

private class TestSettings(
    private val settings: Settings
) : Settings by settings {
    var enumValue by enumSettingOf(TestEnum.One) {
        TestEnum.valueOf(it)
    }
    var intValue by intSetting()
    var longValue by longSetting()
    var floatValue by floatSetting()
    var doubleValue by doubleSetting()
    var stringValue by stringSetting()
    var booleanValue by booleanSetting()
}

public class TestSettingsUtils : StringSpec({
    "test enumSettingOf" {
        val backingMap: MutableMap<String, Any> = mutableMapOf()
        val testSettings = TestSettings(MapSettings(backingMap))
        testSettings.enumValue shouldBe TestEnum.One
        backingMap["enumValue"] shouldBe null

        testSettings.enumValue = TestEnum.Two
        testSettings.enumValue shouldBe TestEnum.Two
        backingMap["enumValue"] shouldBe "Two"
    }

    "test intSetting" {
        val backingMap: MutableMap<String, Any> = mutableMapOf()
        val testSettings = TestSettings(MapSettings(backingMap))
        testSettings.intValue shouldBe 0
        backingMap["intValue"] shouldBe null

        testSettings.intValue = 1
        testSettings.intValue shouldBe 1
        backingMap["intValue"] shouldBe 1

        backingMap["intValue"] = "2"
        testSettings.intValue shouldBe 2
        backingMap["intValue"] shouldBe "2"

        testSettings.intValue = 3
        testSettings.intValue shouldBe 3
        backingMap["intValue"] shouldBe 3
    }
})
