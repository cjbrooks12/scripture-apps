package com.copperleaf.biblebits.platform.services.pubsub

import com.copperleaf.biblebits.platform.services.PubSubService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.transformWhile
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class InMemoryPubSubService : PubSubService {

    private val mutex = Mutex()
    private val map = HashMap<String, MutableSharedFlow<PubSubService.Event>>()

    override suspend fun publish(key: String, event: PubSubService.Event) {
        val subscription = mutex.withLock {
            map.getOrPut(key) { MutableSharedFlow() }
        }
        subscription.emit(event)
    }

    override suspend fun subscribe(key: String, terminateOnClose: Boolean): Flow<PubSubService.Event> {
        val subscription = mutex.withLock {
            map.getOrPut(key) { MutableSharedFlow() }
        }
        return subscription.asSharedFlow()
            .transformWhile {
                if (terminateOnClose) {
                    when (it) {
                        is PubSubService.Event.Message -> {
                            emit(it)
                            true
                        }

                        is PubSubService.Event.Close -> {
                            false
                        }
                    }
                } else {
                    emit(it)
                    true
                }
            }
    }
}
