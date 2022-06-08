plugins {
    kotlin("multiplatform")
    `copper-leaf-base`
    `copper-leaf-version`
    `copper-leaf-lint`
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
                api(libs.ballast.core)
                api(libs.ballast.savedState)
                api(libs.ballast.repository)
                api(libs.ktor.http.utils)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmMain by getting {
            dependencies {
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
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
