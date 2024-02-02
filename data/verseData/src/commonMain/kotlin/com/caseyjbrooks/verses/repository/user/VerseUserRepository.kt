package com.caseyjbrooks.verses.repository.user

import com.caseyjbrooks.verses.models.VerseUser

public interface VerseUserRepository {
    public suspend fun getUserProfile(): VerseUser?
}
