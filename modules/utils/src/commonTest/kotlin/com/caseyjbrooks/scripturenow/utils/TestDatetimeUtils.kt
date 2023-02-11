package com.caseyjbrooks.scripturenow.utils

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import java.time.Month

public class TestDatetimeUtils : StringSpec({
    val fakeClock = object : Clock {
        override fun now(): Instant {
            return LocalDateTime(2022, Month.JANUARY, 1, 0, 0, 0, 0).toInstant(TimeZone.currentSystemDefault())
        }
    }
    "test localDate" {
        LocalDate.now(fakeClock) shouldBe LocalDate(2022, Month.JANUARY, 1)
        LocalDate.now(fakeClock).atStartOfDay() shouldBe LocalDateTime(2022, Month.JANUARY, 1, 0, 0, 0, 0)
    }
    "test localDateTime" {
        LocalDateTime.now(fakeClock) shouldBe LocalDateTime(2022, Month.JANUARY, 1, 0, 0, 0, 0)
    }
})
