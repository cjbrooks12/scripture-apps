package com.copperleaf.ballast.queue

public interface QueueStateStore {
    suspend fun saveState(queueName: String, journeyId: String, payload: String)
    suspend fun getState(queueName: String, journeyId: String): String?
}
