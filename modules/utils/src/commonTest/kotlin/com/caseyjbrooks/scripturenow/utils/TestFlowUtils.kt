package com.caseyjbrooks.scripturenow.utils

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList

public class TestFlowUtils : StringSpec({
    "test mapEach" {
        flowOf(
            listOf(1, 2, 3),
            listOf(4, 5, 6),
            listOf(7, 8, 9),
        ).mapEach { "$it" }.toList() shouldBe listOf(
            listOf("1", "2", "3"),
            listOf("4", "5", "6"),
            listOf("7", "8", "9"),
        )
    }
    "test mapIfNotNull" {
        flowOf(1, 2, null, 4).mapIfNotNull { "$it" }.toList() shouldBe listOf("1", "2", null, "4")
    }
})
