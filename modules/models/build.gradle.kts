plugins {
    `copper-leaf-android-library`
    `copper-leaf-targets`
    `copper-leaf-base`
    `copper-leaf-version`
    `copper-leaf-testing`
    `copper-leaf-lint`
    kotlin("plugin.serialization")
}
android {
    namespace = "com.caseyjbrooks.scripturenow.models"
}

kotlin {
    sourceSets {
        all {
            languageSettings {
                optIn("kotlinx.serialization.ExperimentalSerializationApi")
            }
        }

        // Common Sourcesets
        val commonMain by getting {
            dependencies {
                api(libs.ktor.client.serialization)
                api(libs.ktor.client.serialization.json)
                api(libs.kotlinx.datetime)
                api(libs.benasher44.uuid)
                api(libs.ballast.navigation)
                api(libs.ballast.repository)
            }
        }
        val commonTest by getting {
            dependencies { }
        }

        // plain JVM Sourcesets
        val jvmMain by getting {
            dependencies { }
        }
        val jvmTest by getting {
            dependencies {
                implementation(libs.multiplatformSettings.test)
            }
        }

        // Android JVM Sourcesets
        val androidMain by getting {
            dependencies { }
        }
        val androidUnitTest by getting {
            dependencies { }
        }
    }
}
