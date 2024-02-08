package com.caseyjbrooks.debug.screens.devinfo

internal actual suspend fun getWorkManagerJobInfo(): List<WorkManagerJobInfo> {
    return emptyList()
}

internal actual suspend fun testWorkManagerJob(info: WorkManagerJobInfo) {
}
