plugins {
    alias(libs.plugins.application)
    kotlin("jvm")
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ktor)
//    alias(libs.plugins.ksp)
}

group = "com.copperleaf.biblebits"
version = "0.0.1"

dependencies {
    implementation(project(":lib:ktor-server-platform"))
    implementation(project(":lib:dto"))
}

kotlin {
    compilerOptions {
        optIn = listOf("io.lettuce.core.ExperimentalLettuceCoroutinesApi")
    }
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

ktor {
    fatJar {
    }
}
