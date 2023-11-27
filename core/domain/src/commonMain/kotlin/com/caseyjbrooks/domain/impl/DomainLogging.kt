package com.caseyjbrooks.domain.impl

import com.caseyjbrooks.domain.DomainActionWrapper
import com.caseyjbrooks.domain.DomainQueryWrapper
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.cancellation.CancellationException

public class DomainLogging<Result : Any>(
    private val name: String,
    private val logger: (String) -> Unit,
) : DomainActionWrapper<Result>, DomainQueryWrapper<Result> {
    override suspend fun executeAction(block: suspend () -> Result): Result {
        return wrapWithLogging { block() }
    }

    override fun executeQuery(block: () -> Flow<Result>): Flow<Result> {
        return wrapWithLogging { block() }
    }

    private inline fun <T> wrapWithLogging(block: () -> T): T {
        try {
            logger("[$name] before")
            val result = block()
            logger("[$name] success")
            return result
        } catch (e: CancellationException) {
            logger("[$name] cancelled")
            throw e
        } catch (e: Throwable) {
            logger("[$name] failure: ${e.message}")
            throw e
        }
    }
}
