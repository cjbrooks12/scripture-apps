package com.caseyjbrooks.verses.repository.config

internal class FakeVerseConfig(
    override val maxVersesOnFreePlan: Int = 10
) : VerseConfig
