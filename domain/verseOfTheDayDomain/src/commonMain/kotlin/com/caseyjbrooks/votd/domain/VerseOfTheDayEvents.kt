package com.caseyjbrooks.votd.domain

public sealed interface VerseOfTheDayDomainEvents {
    public data object VerseOfTheDayUpdated : VerseOfTheDayDomainEvents
}
