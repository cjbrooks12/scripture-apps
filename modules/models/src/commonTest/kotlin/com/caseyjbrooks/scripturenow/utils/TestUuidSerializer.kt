package com.caseyjbrooks.scripturenow.utils

import com.benasher44.uuid.uuidFrom
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import kotlinx.serialization.json.Json

public class TestUuidSerializer : StringSpec({
    "test parse" {
        val input = uuidFrom("571c0916-bcad-43e9-b550-8bd0d367f118")
        input.toString() shouldBe "571c0916-bcad-43e9-b550-8bd0d367f118"

        val encoded = Json.encodeToString(UuidSerializer, input)
        encoded shouldBe "\"571c0916-bcad-43e9-b550-8bd0d367f118\""

        val decoded = Json.decodeFromString(UuidSerializer, encoded)
        decoded shouldBe input
        decoded shouldNotBeSameInstanceAs input
    }
})
