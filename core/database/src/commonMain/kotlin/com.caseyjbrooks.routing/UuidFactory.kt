package com.caseyjbrooks.routing

import com.benasher44.uuid.uuid4

public fun interface UuidFactory {
    public fun getNewUuid(): String
}

public interface UuidFactoryImpl : UuidFactory {
    override fun getNewUuid(): String {
        return uuid4().toString()
    }
}
