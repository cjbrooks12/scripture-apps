plugins {
    kotlin("jvm")
    alias(libs.plugins.kotlinx.serialization)
//    alias(libs.plugins.ksp)
}

group = "com.copperleaf.biblebits"
version = "0.0.1"

dependencies {
    api(platform(libs.ktor.bom))

    api("io.ktor:ktor-server-core")
    api("io.ktor:ktor-serialization-kotlinx-json")
    api("io.ktor:ktor-server-content-negotiation")

    api("io.ktor:ktor-server-metrics")
    api("io.ktor:ktor-server-metrics-micrometer")
    api("io.micrometer:micrometer-registry-prometheus:1.14.1")

    api("io.ktor:ktor-server-auth")
    api("io.ktor:ktor-server-auth-jwt")

    api("io.ktor:ktor-server-call-logging")
    api("io.ktor:ktor-server-call-id")
//    api("io.ktor:ktor-server-host")
    api("io.ktor:ktor-server-status-pages")
    api("io.ktor:ktor-server-double-receive")
    api("io.ktor:ktor-server-auto-head-response")
    api("io.ktor:ktor-server-netty")
    api("io.ktor:ktor-server-websockets")
    api("io.ktor:ktor-server-cors")
    api("org.apache.logging.log4j:log4j-api:2.24.1")
    api("org.apache.logging.log4j:log4j-core:2.24.1")
    api("org.apache.logging.log4j:log4j-slf4j2-impl:2.24.1")
    api("io.ktor:ktor-server-config-yaml")
//    testImplementation("io.ktor:ktor-server-tests")
//    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    api("io.ktor:ktor-client-core")
    api("io.ktor:ktor-client-cio")
    api("io.ktor:ktor-client-content-negotiation")
    api("io.ktor:ktor-client-logging")

    // database
    api("org.flywaydb:flyway-core:6.5.2")
    api("org.postgresql:postgresql:42.7.3")
    api("com.zaxxer:HikariCP:5.1.0")
    api("com.benasher44:uuid:0.8.4")
    api("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")
    api("io.lettuce:lettuce-core:6.3.2.RELEASE")
    api("com.ucasoft.ktor:ktor-simple-cache:0.3.1")
    api("com.ucasoft.ktor:ktor-simple-redis-cache:0.3.1")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:1.8.1")

    // dependency injection
    api("io.insert-koin:koin-core:3.5.6")
    api("io.insert-koin:koin-ktor:3.5.6")
//    api("io.insert-koin:koin-annotations:1.3.1")
//    ksp("io.insert-koin:koin-ksp-compiler:1.3.1")

    api("io.github.copper-leaf:ballast-core:4.2.1")
    api("io.github.copper-leaf:ballast-schedules:4.2.1")
}

kotlin {
    compilerOptions {
        optIn = listOf("io.lettuce.core.ExperimentalLettuceCoroutinesApi")
    }
}
