package com.caseyjbrooks.platform.services.blob

import com.caseyjbrooks.platform.services.BlobStorageService
import kotlinx.io.buffered
import kotlinx.io.files.FileSystem
import kotlinx.io.files.Path
import kotlinx.io.readByteArray

class FilesystemBlobStorageService(
    private val filesystem: FileSystem,
    private val rootDir: Path,
) : BlobStorageService {
    override suspend fun getBlob(path: String): ByteArray? {
        val filesystemPath = Path(rootDir, path)

        return if (filesystem.exists(filesystemPath)) {
            filesystem.source(filesystemPath).buffered().use {
                it.readByteArray()
            }
        } else {
            null
        }
    }

    override suspend fun putBlob(path: String, bytes: ByteArray) {
        val filesystemPath = Path(rootDir, path)

        filesystem.createDirectories(filesystemPath.parent!!, mustCreate = false)
        return filesystem.sink(filesystemPath).buffered().use {
            it.write(bytes)
        }
    }
}
