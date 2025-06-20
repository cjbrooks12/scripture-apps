package com.copperleaf.biblebits.platform.services

interface BlobStorageService {
    suspend fun getBlob(path: String): ByteArray?
    suspend fun putBlob(path: String, bytes: ByteArray)
}
