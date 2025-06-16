package com.copperleaf.ballast.queue

public interface QueueDriver {
    suspend fun addToQueue(item: SerializedJob)
    suspend fun pollNext(queueName: String): SerializedJob?
}
