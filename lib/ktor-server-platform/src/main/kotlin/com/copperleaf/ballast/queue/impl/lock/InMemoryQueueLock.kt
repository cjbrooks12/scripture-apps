package com.copperleaf.ballast.queue.impl.lock

import com.copperleaf.ballast.queue.QueueLock
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class InMemoryQueueLock : QueueLock {
    private val internalLock = Mutex()
    private val jobLocks: MutableMap<String, Mutex> = mutableMapOf()
    private val journeyLocks: MutableMap<String, Mutex> = mutableMapOf()

    override suspend fun lockJob(jobId: String) {
        val jobLock = internalLock.withLock {
            jobLocks.getOrPut(jobId) { Mutex() }
        }
        jobLock.lock()
    }

    override suspend fun unlockJob(jobId: String) {
        val jobLock = internalLock.withLock {
            jobLocks[jobId]
        }
        if (jobLock == null) {
            error("Lock for job ID $jobId not found")
        } else {
            jobLock.unlock()
        }
    }

    override suspend fun lockJourney(journeyId: String) {
        val journeyLock = internalLock.withLock {
            journeyLocks.getOrPut(journeyId) { Mutex() }
        }
        journeyLock.lock()
    }

    override suspend fun unlockJourney(journeyId: String) {
        val journeyLock = internalLock.withLock {
            journeyLocks[journeyId]
        }
        if (journeyLock == null) {
            error("Lock for journey ID $journeyId not found")
        } else {
            journeyLock.unlock()
        }
    }
}
