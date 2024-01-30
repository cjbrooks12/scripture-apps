package com.caseyjbrooks.database

import app.cash.sqldelight.db.AfterVersion
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import org.w3c.dom.Worker

internal fun WebWorkerDriver(
    worker: Worker,
    schema: SqlSchema<QueryResult.AsyncValue<Unit>>,
    migrateEmptySchema: Boolean = false,
    vararg callbacks: AfterVersion,
): WebWorkerDriver {
    val driver = WebWorkerDriver(worker)
    val version = driver.getVersion()

    if (version == 0L && !migrateEmptySchema) {
        schema.create(driver).value
        driver.setVersion(schema.version)
    } else if (version < schema.version) {
        schema.migrate(driver, version, schema.version, *callbacks).value
        driver.setVersion(schema.version)
    }

    return driver
}

private fun WebWorkerDriver.getVersion(): Long {
    val mapper = { cursor: SqlCursor ->
        QueryResult.Value(if (cursor.next().value) cursor.getLong(0) else null)
    }
    return executeQuery(null, "PRAGMA user_version", mapper, 0, null).value ?: 0L
}

private fun WebWorkerDriver.setVersion(version: Long) {
    execute(null, "PRAGMA user_version = $version", 0, null).value
}
