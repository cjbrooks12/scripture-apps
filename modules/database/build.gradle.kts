plugins {
    `copper-leaf-android-library`
    `copper-leaf-targets`
    `copper-leaf-base`
    `copper-leaf-version`
    `copper-leaf-testing`
    `copper-leaf-lint`
    id("com.squareup.sqldelight")
}

kotlin {
    sourceSets {
        // Common Sourcesets
        val commonMain by getting {
            dependencies {
                api(project(":modules:config"))
                implementation(libs.sqldelight.coroutines)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.sqldelight.jvm)
            }
        }

        // plain JVM Sourcesets
        val jvmMain by getting {
            dependencies {
                implementation(libs.sqldelight.jvm)
            }
        }
        val jvmTest by getting {
            dependencies { }
        }

        // Android JVM Sourcesets
        val androidMain by getting {
            dependencies {
                api(libs.sqldelight.android)
            }
        }
        val androidUnitTest by getting {
            dependencies { }
        }
    }
}

sqldelight {
    database("ScriptureNowDatabase") {
        packageName = "com.caseyjbrooks.scripturenow"
    }
}
