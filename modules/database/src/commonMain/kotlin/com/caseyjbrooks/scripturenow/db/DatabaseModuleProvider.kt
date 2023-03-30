package com.caseyjbrooks.scripturenow.db

import com.caseyjbrooks.scripturenow.ScriptureNowDatabase
import com.caseyjbrooks.scripturenow.config.local.LocalAppConfig
import com.squareup.sqldelight.db.SqlDriver

public interface DatabaseModuleProvider {
    public fun getDatabase(
        driver: SqlDriver,
        config: LocalAppConfig,
    ): ScriptureNowDatabase
}
