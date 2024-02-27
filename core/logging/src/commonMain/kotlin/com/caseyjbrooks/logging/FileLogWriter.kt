package com.caseyjbrooks.logging

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Severity

internal class FileLogWriter(private val delegate: LogWriter) : LogWriter() {
    override fun isLoggable(tag: String, severity: Severity): Boolean {
        return delegate.isLoggable(tag, severity)
    }

    override fun log(severity: Severity, message: String, tag: String, throwable: Throwable?) {
        delegate.log(severity, message, tag, throwable)
    }
}
