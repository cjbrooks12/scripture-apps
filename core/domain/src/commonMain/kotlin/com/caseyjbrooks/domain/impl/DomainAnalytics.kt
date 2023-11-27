package com.caseyjbrooks.domain.impl

import com.caseyjbrooks.domain.DomainActionWrapper
import com.caseyjbrooks.domain.DomainQueryWrapper
import kotlinx.coroutines.flow.Flow

public class DomainAnalytics<Result : Any>(
    private val eventName: String,
    private val analyticsTracker: (String) -> Unit,
) : DomainActionWrapper<Result>, DomainQueryWrapper<Result> {

    override suspend fun executeAction(block: suspend () -> Result): Result {
        return wrapWithAnalytics { block() }
    }

    override fun executeQuery(block: () -> Flow<Result>): Flow<Result> {
        return wrapWithAnalytics { block() }
    }

    private inline fun <T> wrapWithAnalytics(block: () -> T): T {
        val result = block()
        analyticsTracker(eventName)
        return result
    }
}
