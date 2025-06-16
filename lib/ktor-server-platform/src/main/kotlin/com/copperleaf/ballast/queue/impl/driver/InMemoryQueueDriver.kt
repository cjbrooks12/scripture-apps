package com.copperleaf.ballast.queue.impl.driver

import com.copperleaf.ballast.queue.QueueDriver
import com.copperleaf.ballast.queue.SerializedJob
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class InMemoryQueueDriver(
    private val clock: Clock,
    private val delayRetries: Boolean = false,
) : QueueDriver {
    private val mutex = Mutex()
    private val queue: MutableList<SerializedJob> = mutableListOf()

    override suspend fun addToQueue(item: SerializedJob) {
        mutex.withLock {
            queue.add(item)
        }
    }

    override suspend fun pollNext(queueName: String): SerializedJob? {
        val item = mutex.withLock {
            val now = clock.now()

            val item = queue
                .asSequence()
                .filter { it.queueName == queueName }
                .filter { isReady(it, now) }
                .sortedByDescending { it.insertedAt }
                .maxByOrNull { it.priority }

            if (item != null) {
                queue.remove(item)
            }
            item
        }

        return item
    }

    private fun isReady(item: SerializedJob, now: Instant) : Boolean {
        val nextInvocation = if(item.lastAttemptAt != null) {
            if(delayRetries) {
                item.lastAttemptAt + item.delay
            } else {
                item.insertedAt + item.delay
            }
        } else {
            item.insertedAt + item.delay
        }
        return nextInvocation <= now
    }
}
