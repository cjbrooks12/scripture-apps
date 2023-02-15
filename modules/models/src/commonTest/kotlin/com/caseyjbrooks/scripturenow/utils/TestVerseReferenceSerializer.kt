package com.caseyjbrooks.scripturenow.utils

import com.caseyjbrooks.scripturenow.models.VerseReference
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import kotlinx.serialization.json.Json

public class TestVerseReferenceSerializer : StringSpec({
    "test parse" {
        val input = "John 3:16".parseVerseReference()
        input shouldBe VerseReference.KnownReference("John", 3, 16..16)

        val encoded = Json.encodeToString(VerseReferenceSerializer, input)
        encoded shouldBe "\"John 3:16\""

        val decoded = Json.decodeFromString(VerseReferenceSerializer, encoded)
        decoded shouldBe VerseReference.KnownReference("John", 3, 16..16)
        decoded shouldBe input
        decoded shouldNotBeSameInstanceAs input
    }
})
