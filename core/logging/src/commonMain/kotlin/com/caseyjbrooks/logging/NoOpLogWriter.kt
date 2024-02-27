package com.caseyjbrooks.logging

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Severity

internal class NoOpLogWriter : LogWriter() {
    override fun isLoggable(tag: String, severity: Severity): Boolean {
        return false
    }

    override fun log(severity: Severity, message: String, tag: String, throwable: Throwable?) {
    }
}
