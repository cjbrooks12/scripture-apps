package com.caseyjbrooks.prayer.models

import kotlin.jvm.JvmInline

/**
 * A tag that users may use for categorizing prayers in their collection.
 */
@JvmInline
public value class PrayerTag(
    public val tag: String,
)
