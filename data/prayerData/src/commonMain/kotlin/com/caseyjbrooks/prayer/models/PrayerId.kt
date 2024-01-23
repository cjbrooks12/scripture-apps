package com.caseyjbrooks.prayer.models

import com.benasher44.uuid.Uuid
import kotlin.jvm.JvmInline

/**
 * A String UUID value uniquely identifying this prayer among this user's collection.
 */
@JvmInline
public value class PrayerId(
    public val uuid: Uuid,
)
