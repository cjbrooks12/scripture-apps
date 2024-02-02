package com.caseyjbrooks.verses.models

import com.benasher44.uuid.Uuid

/**
 * A String UUID value uniquely identifying this verse among this user's collection.
 */
@JvmInline
public value class VerseId(
    public val uuid: Uuid,
)
