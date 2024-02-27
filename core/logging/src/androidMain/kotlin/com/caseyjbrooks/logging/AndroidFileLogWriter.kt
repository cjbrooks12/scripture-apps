package com.caseyjbrooks.logging

import android.content.Context
import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Message
import co.touchlab.kermit.MessageStringFormatter
import co.touchlab.kermit.Severity
import co.touchlab.kermit.Tag
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.io.File
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * This class writes log messages to a specified [File], which should be in the app's cache directory. Messages are
 * formatted in the callling thread, but the actual file writing happens on a background thread. Messages are formatted,
 * sent into a Channel, and then written to the channel from a coroutine running on the [ioDispatcher], so that the file
 * IO does not impact performance.
 */
public class AndroidFileLogWriter(
    private val context: Context,
    private val clock: Clock,
    private val timeZone: TimeZone,
    coroutineScope: CoroutineScope,
    ioDispatcher: CoroutineDispatcher,
) : LogWriter() {
    private val messageStringFormatter: MessageStringFormatter = FileLineLogMessageStringFormatter(clock, timeZone)
    private val logChannel: Channel<String> = Channel(Channel.UNLIMITED)

    init {
        coroutineScope.launch(ioDispatcher) {
            logChannel
                .receiveChunked(timeout = 10.seconds, maxChunkSize = 100)
                .onEach { chunk ->
                    getOrCreateCacheLogFile().printWriter().use { writer ->
                        println("Writing log batch: ${chunk.size} logs")
                        chunk.forEach { logMessage ->
                            writer.println(logMessage)
                        }
                    }
                }
                .launchIn(this)
        }
    }

    override fun log(severity: Severity, message: String, tag: String, throwable: Throwable?) {
        logChannel.trySend(messageStringFormatter.formatMessage(severity, Tag(tag), Message(message)))
    }

// get log file
// ---------------------------------------------------------------------------------------------------------------------

    private fun getOrCreateCacheLogFile(): File {
        val now: Instant = clock.now()
        val nowDate: LocalDate = now.toLocalDateTime(timeZone).date
        val logFileName: String = with(nowDate) { "$year-$monthNumber-$dayOfMonth-$dayOfWeek.log" }

        val cacheFile = File(context.cacheDir, logFileName)
        if (!cacheFile.exists()) {
            cacheFile.createNewFile()
        }

        return cacheFile
    }

// Time-chunked Channel processing
// ---------------------------------------------------------------------------------------------------------------------

    /**
     * Consumes items from the input Channel, and attempts to send them downstream into the flow in chunks to optimize
     * processing time.
     *
     * A List of items will be sent when either:
     *   - [maxChunkSize] items have been received from the channel, or
     *   - [timeout] time has passed since the last item was sent to the channel
     *
     * When either condition has occurred, the current chunk of items will be sent downstream for processing.
     */
    private suspend fun <T> ReceiveChannel<T>.receiveChunked(
        timeout: Duration,
        maxChunkSize: Int,
    ): Flow<List<T>> {
        val channel = this
        return flow {
            val currentChunk = mutableListOf<T>()
            var timedOut: Boolean = false

            while (true) {
                if ((timedOut && currentChunk.isNotEmpty()) || (currentChunk.size >= maxChunkSize)) {
                    // we have collected the full chunk, or hit the timeout. Send the chunk downstream, then clear the
                    // chunk to accept more items, and resent the timedOut flag
                    emit(currentChunk.toList())
                    currentChunk.clear()
                    timedOut = false
                }

                if (channel.isClosedForReceive) {
                    // the channel was closed. Break out of the loop to terminate this flow
                    break
                } else {
                    // suspend until we receive an item through the channel, or until the timout has been reached
                    val nextItem = withTimeoutOrNull(timeout) {
                        channel.receive()
                    }

                    if (nextItem != null) {
                        // we received an item within the specified timeout. Simply add it to the chunk, then continue
                        // the loop to add more items into that chunk before processing
                        currentChunk.add(nextItem)
                    } else {
                        // we did not receive another item during the specified timeout. Continue the loop, so that the
                        // next iteration will send the chunk downstream
                        timedOut = true
                    }
                }
            }
        }
    }
}
