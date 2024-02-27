package com.caseyjbrooks.logging

import co.touchlab.kermit.Message
import co.touchlab.kermit.MessageStringFormatter
import co.touchlab.kermit.Severity
import co.touchlab.kermit.Tag
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal class FileLineLogMessageStringFormatter(
    private val clock: Clock,
    private val timeZone: TimeZone,
    private val tagLength: Int = 30,
) : MessageStringFormatter {
    override fun formatMessage(severity: Severity?, tag: Tag?, message: Message): String {
        return buildString {
            // datetime
            append("${clock.now().toLocalDateTime(timeZone)} ")

            // tag
            append("[${(tag?.tag ?: "").padStart(tagLength).substring(0, tagLength)}] ")

            // severity
            append("${(severity?.name ?: "NULL").padStart(7)} ")

            // message
            append(message.message)
        }
    }
}
