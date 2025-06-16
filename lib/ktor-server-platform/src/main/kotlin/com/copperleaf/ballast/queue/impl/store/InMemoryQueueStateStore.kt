package com.copperleaf.ballast.queue.impl.store

import com.copperleaf.ballast.queue.QueueStateStore

class InMemoryQueueStateStore : QueueStateStore {
    private val store: MutableMap<String, MutableMap<String, String>> = mutableMapOf()

    override suspend fun getState(queueName: String, journeyId: String): String? {
        val topicStore = store.getOrPut(queueName) { mutableMapOf() }
        return topicStore[journeyId]
    }

    override suspend fun saveState(queueName: String, journeyId: String, payload: String) {
        val topicStore = store.getOrPut(queueName) { mutableMapOf() }
        topicStore[journeyId] = payload
    }
}
