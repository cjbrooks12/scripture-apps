plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    `copper-leaf-base`
    `copper-leaf-version`
    `copper-leaf-lint`
    id("com.google.devtools.ksp")
    id("com.squareup.sqldelight")
}

var projectVersion: ProjectVersion by project.extra
description = "Opinionated Application State Management framework for Kotlin Multiplatform"

// Kotlin config
// ---------------------------------------------------------------------------------------------------------------------

kotlin {
    // targets
    jvm { }
    js(IR) {
        browser {
            testTask {
                enabled = false
            }
        }
    }
    ios { }

    // sourcesets
    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlin.time.ExperimentalTime")
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
            }
        }

        // Common Sourcesets
        val commonMain by getting {
            dependencies {
                api(project(":modules:common"))
                api(project(":modules:votd:api"))

                api(libs.kotlinx.serialization.json)
                api(libs.ksp.ktorfit.runtime)
                api(libs.sqldelight.coroutines)
            }
        }

        val jvmMain by getting {
            kotlin.srcDir("build/generated/ksp/jvm/jvmMain/kotlin")
            dependencies {
                api("de.brudaswen.kotlinx.serialization:kotlinx-serialization-csv:1.1.0")
            }
        }

        val jsMain by getting {
            dependencies {
            }
        }

        val iosMain by getting {
            dependencies {
            }
        }
    }
}

tasks.withType<JavaCompile> {
    sourceCompatibility = Config.javaVersion
    targetCompatibility = Config.javaVersion
}
tasks.withType<Test> {
    testLogging {
        showStandardStreams = true
    }
}
