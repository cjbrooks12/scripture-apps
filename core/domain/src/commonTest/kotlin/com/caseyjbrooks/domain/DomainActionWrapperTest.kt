package com.caseyjbrooks.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class DomainActionWrapperTest : StringSpec({
    "DomainAction0 wrapping" {
        val kernel: DomainAction0<String> = { "[kernel]" }
        wrapDomainAction(
            kernel,
            CombinedDomainActionWrapper(
                listOf(
                    Wrapper("1", "1"),
                    Wrapper("2", "2"),
                    Wrapper("a", "A"),
                    Wrapper("b", "B"),
                ),
            ),
        ).let { wrapped -> wrapped() shouldBe "ba21[kernel]12AB" }
    }

    "DomainAction1 wrapping" {
        val kernel: DomainAction1<String, String> = { p1 -> "[$p1]" }
        wrapDomainAction(
            kernel,
            CombinedDomainActionWrapper(
                listOf(
                    Wrapper("1", "1"),
                    Wrapper("2", "2"),
                    Wrapper("a", "A"),
                    Wrapper("b", "B"),
                ),
            ),
        ).let { wrapped -> wrapped("kernel") shouldBe "ba21[kernel]12AB" }
    }
}) {
    data class Wrapper(
        val prepend: String,
        val append: String,
    ) : DomainActionWrapper<String> {
        override suspend fun executeAction(block: suspend () -> String): String {
            return prepend + super.executeAction(block) + append
        }
    }
}
