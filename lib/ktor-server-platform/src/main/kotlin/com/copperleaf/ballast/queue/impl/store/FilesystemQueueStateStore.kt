package com.copperleaf.ballast.queue.impl.store

import com.copperleaf.ballast.queue.QueueStateStore
import io.ktor.utils.io.core.readText
import io.ktor.utils.io.core.writeText
import kotlinx.io.buffered
import kotlinx.io.files.FileSystem
import kotlinx.io.files.Path

class FilesystemQueueStateStore(
    private val filesystem: FileSystem,
    private val rootDir: Path,
) : QueueStateStore {

    override suspend fun getState(queueName: String, journeyId: String): String? {
        val filesystemPath = Path(rootDir, "$journeyId.json")

        return if (filesystem.exists(filesystemPath)) {
            filesystem.source(filesystemPath).buffered().use {
                it.readText()
            }
        } else {
            null
        }
    }

    override suspend fun saveState(queueName: String, journeyId: String, payload: String) {
        val filesystemPath = Path(rootDir, "$journeyId.json")

        filesystem.createDirectories(filesystemPath.parent!!, mustCreate = false)
        return filesystem.sink(filesystemPath).buffered().use {
            it.writeText(payload)
        }
    }
}
