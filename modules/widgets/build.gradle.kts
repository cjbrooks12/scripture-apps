plugins {
    `copper-leaf-android-library`
    `copper-leaf-targets`
    `copper-leaf-base`
    `copper-leaf-version`
    `copper-leaf-testing`
    `copper-leaf-lint`
    `copper-leaf-compose`
}

kotlin {
    sourceSets {
        all {
            languageSettings.apply {
            }
        }

        // Common Sourcesets
        val commonMain by getting {
            dependencies {
                api(project(":modules:repositories"))
            }
        }
        val commonTest by getting {
            dependencies { }
        }

        // Android JVM Sourcesets
        val androidMain by getting {
            dependencies {
                implementation(libs.android.compose.glance)
            }
        }
        val androidUnitTest by getting {
            dependencies { }
        }
    }
}
