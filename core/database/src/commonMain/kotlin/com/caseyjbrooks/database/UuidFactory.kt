package com.caseyjbrooks.database

import com.benasher44.uuid.uuid4

public fun interface UuidFactory {
    public fun getNewUuid(): String
}

public class UuidFactoryImpl : UuidFactory {
    override fun getNewUuid(): String {
        return uuid4().toString()
    }
}

