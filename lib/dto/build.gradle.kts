@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    jvm { }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    )

    wasmJs {
        useEsModules()
        browser()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
                api("com.benasher44:uuid:0.8.4")
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")
            }
        }
        val jvmMain by getting {
            dependencies {
            }
        }
        val wasmJsMain by getting {
            dependencies {
            }
        }
    }
}
