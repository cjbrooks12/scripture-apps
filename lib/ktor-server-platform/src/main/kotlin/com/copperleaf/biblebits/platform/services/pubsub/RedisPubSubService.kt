package com.copperleaf.biblebits.platform.services.pubsub

import com.copperleaf.biblebits.platform.services.PubSubService
import io.lettuce.core.RedisClient
import io.lettuce.core.api.coroutines
import io.lettuce.core.pubsub.RedisPubSubAdapter
import io.lettuce.core.pubsub.RedisPubSubListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.json.Json

class RedisPubSubService(
    private val redisClient: RedisClient,
    private val json: Json,
) : PubSubService {

    override suspend fun publish(key: String, event: PubSubService.Event) {
        redisClient.connectPubSub().use { connection ->
            connection.coroutines().publish(key, json.encodeToString(PubSubService.Event.serializer(), event))
        }
    }

    override suspend fun subscribe(key: String, terminateOnClose: Boolean): Flow<PubSubService.Event> {
        return callbackFlow {
            val connection = redisClient.connectPubSub()
            val sync = connection.sync()

            val scope = this
            val listener: RedisPubSubListener<String, String> = object : RedisPubSubAdapter<String, String>() {
                override fun message(channel: String, message: String) {
                    val event = json.decodeFromString(PubSubService.Event.serializer(), message)

                    scope.channel.trySendBlocking(event)

                    if (terminateOnClose && event is PubSubService.Event.Close) {
                        scope.channel.close()
                    }
                }
            }

            connection.addListener(listener)
            sync.subscribe(key)

            awaitClose {
                connection.removeListener(listener)
                connection.close()
            }
        }
    }
}
