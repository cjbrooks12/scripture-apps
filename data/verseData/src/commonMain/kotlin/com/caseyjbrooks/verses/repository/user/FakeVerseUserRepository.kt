package com.caseyjbrooks.verses.repository.user

import com.caseyjbrooks.verses.models.VerseUser

internal class FakeVerseUserRepository(
    private val hardcodedUser: VerseUser?,
) : VerseUserRepository {
    override suspend fun getUserProfile(): VerseUser? {
        return hardcodedUser
    }
}
