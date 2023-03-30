package com.caseyjbrooks.scripturenow.utils

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

public class TestRangeUtils : StringSpec({
    "test range size (x..y)" {
        (1..1).size shouldBe 1
        (1..2).size shouldBe 2
        (2..2).size shouldBe 1
        (-1..1).size shouldBe 3
    }
    "test range size (x until y)" {
        (1 until 1).size shouldBe 0
        (1 until 2).size shouldBe 1
        (2 until 2).size shouldBe 0
        (-1 until 1).size shouldBe 2
    }
})
