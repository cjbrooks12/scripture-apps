package com.caseyjbrooks.di

public data class Variant(
    val environment: Environment,
    val buildType: BuildType,
) {
    public enum class Environment {
        Test,
        Local,
        Qa,
        Production,
    }

    public enum class BuildType {
        Debug,
        Release,
    }
}
