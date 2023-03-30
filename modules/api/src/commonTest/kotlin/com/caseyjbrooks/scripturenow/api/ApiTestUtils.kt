package com.caseyjbrooks.scripturenow.api

import io.kotest.core.test.Enabled
import io.kotest.core.test.EnabledOrReasonIf

object ApiTestUtils {
    fun enabledIfEnvironmentVariable(environmentVariableName: String): EnabledOrReasonIf {
        return {
            if (System.getenv(environmentVariableName) != null) {
                Enabled.enabled
            } else {
                Enabled.disabled("environment variable '$environmentVariableName' is not set")
            }
        }
    }
}
