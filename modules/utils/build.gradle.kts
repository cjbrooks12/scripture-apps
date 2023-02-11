plugins {
    `copper-leaf-android-library`
    `copper-leaf-targets`
    `copper-leaf-base`
    `copper-leaf-version`
    `copper-leaf-testing`
    `copper-leaf-lint`
    kotlin("plugin.serialization")
}

kotlin {
    sourceSets {
        // Common Sourcesets
        val commonMain by getting {
            dependencies {
                api(project(":modules:models"))

                api(libs.kotlinx.coroutines.core)
                api(libs.kermit.core)

                api(libs.ballast.core)
                api(libs.ballast.repository)

                api(libs.multiplatformSettings.core)
                api(libs.multiplatformSettings.noArg)

                api(libs.ktor.client.core)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.client.serialization)
                implementation(libs.ktor.client.serialization.json)

                api(libs.kotlinx.serialization.json)
                implementation(libs.jsoup)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.multiplatformSettings.test)
            }
        }

        // plain JVM Sourcesets
        val jvmMain by getting {
            dependencies { }
        }
        val jvmTest by getting {
            dependencies { }
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
