@file:Suppress("UNUSED_VARIABLE")

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

val libs = the<LibrariesForLibs>()
val customProperties = Config.customProperties(project)

@OptIn(ExperimentalComposeLibrary::class)
kotlin {
    sourceSets {
        // Common Sourcesets
        val commonMain by getting {
            dependencies {
                api(compose.material3)
            }
        }
        val commonTest by getting {
            dependencies { }
        }

        if (customProperties["caseyjbrooks.targets.jvm"] == true) {
            val jvmMain by getting {
                dependencies { }
            }
            val jvmTest by getting {
                dependencies { }
            }
        }

        // Android JVM Sourcesets
        if (customProperties["caseyjbrooks.targets.android"] == true) {
            val androidMain by getting {
                dependencies { }
            }
            val androidUnitTest by getting {
                dependencies { }
            }
        }

        if (customProperties["caseyjbrooks.targets.js"] == true) {
            // JS Sourcesets
            val jsMain by getting {
                dependencies { }
            }
            val jsTest by getting {
                dependencies { }
            }
        }

        if (customProperties["caseyjbrooks.targets.ios"] == true) {
            // iOS Sourcesets
            val iosMain by getting {
                dependencies { }
            }
            val iosTest by getting {
                dependencies { }
            }
        }
    }
}

