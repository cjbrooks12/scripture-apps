plugins {
    id("copper-leaf-base")
    id("copper-leaf-android-library")
    id("copper-leaf-targets")
    id("copper-leaf-kotest")
    id("copper-leaf-compose")
//    id("copper-leaf-lint")
    id("copper-leaf-sqldelight")
}

kotlin {
    sourceSets {
        // Common Sourcesets
        val commonMain by getting {
            dependencies {
                api(project(":core:di"))
                api(project(":core:datetime"))
                api(libs.benasher44.uuid)
                api(libs.multiplatformSettings.core)
                api(libs.sqldelight.coroutines)
            }
        }
        val commonTest by getting {
            dependencies { }
        }

        // plain JVM Sourcesets
        val jvmMain by getting {
            dependencies {
                implementation(libs.sqldelight.driver.jvm)
            }
        }
        val jvmTest by getting {
            dependencies { }
        }

        // Android JVM Sourcesets
        val androidMain by getting {
            dependencies {
                implementation(libs.sqldelight.driver.android)
            }
        }
        val androidUnitTest by getting {
            dependencies { }
        }
    }
}

sqldelight {
    databases {
        create("ScriptureNowDatabase") {
            packageName.set("com.caseyjbrooks.database")
        }
    }
}
