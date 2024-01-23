package com.caseyjbrooks.database

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4
import com.benasher44.uuid.uuidFrom

public fun interface UuidFactory {
    public fun getNewUuid(): Uuid
}

public class RealUuidFactory : UuidFactory {
    override fun getNewUuid(): Uuid {
        return uuid4()
    }
}

public class FakeUuidFactory : UuidFactory {
    private var count: Int = 0
    override fun getNewUuid(): Uuid {
        count++
        return uuidFrom("00000000-0000-0000-0000-${count.toString().padStart(12, '0')}")
    }
}

