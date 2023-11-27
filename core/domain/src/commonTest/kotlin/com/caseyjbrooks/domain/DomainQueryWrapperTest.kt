package com.caseyjbrooks.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class DomainQueryWrapperTest : StringSpec({
    "DomainQuery0 wrapping" {
        val kernel: DomainQuery0<String> = { flowOf("[kernel]") }
        wrapDomainQuery(
            kernel,
            CombinedDomainQueryWrapper(
                listOf(
                    Wrapper("1", "1"),
                    Wrapper("2", "2"),
                    Wrapper("a", "A"),
                    Wrapper("b", "B"),
                ),
            ),
        ).let { wrapped -> wrapped().first() shouldBe "ba21[kernel]12AB" }
    }

    "DomainQuery1 wrapping" {
        val kernel: DomainQuery1<String, String> = { p1 -> flowOf("[$p1]") }
        wrapDomainQuery(
            kernel,
            CombinedDomainQueryWrapper(
                listOf(
                    Wrapper("1", "1"),
                    Wrapper("2", "2"),
                    Wrapper("a", "A"),
                    Wrapper("b", "B"),
                ),
            ),
        ).let { wrapped -> wrapped("kernel").first() shouldBe "ba21[kernel]12AB" }
    }
}) {
    data class Wrapper(
        val prepend: String,
        val append: String,
    ) : DomainQueryWrapper<String> {

        override fun executeQuery(block: () -> Flow<String>): Flow<String> {
            return super.executeQuery(block).map {
                prepend + it + append
            }
        }
    }
}
