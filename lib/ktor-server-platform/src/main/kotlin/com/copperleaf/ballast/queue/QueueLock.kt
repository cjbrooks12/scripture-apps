package com.copperleaf.ballast.queue

public interface QueueLock {
    suspend fun lockJob(jobId: String)
    suspend fun unlockJob(jobId: String)

    suspend fun lockJourney(journeyId: String)
    suspend fun unlockJourney(journeyId: String)
}
