package com.caseyjbrooks.database

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class UuidFactoryTest : StringSpec({
    "Test fake UuidFactory" {
        val factory = FakeUuidFactory()
        factory.getNewUuid().toString() shouldBe "00000000-0000-0000-0000-000000000001"
        factory.getNewUuid().toString() shouldBe "00000000-0000-0000-0000-000000000002"
        factory.getNewUuid().toString() shouldBe "00000000-0000-0000-0000-000000000003"
        factory.getNewUuid().toString() shouldBe "00000000-0000-0000-0000-000000000004"
        factory.getNewUuid().toString() shouldBe "00000000-0000-0000-0000-000000000005"
    }
})
