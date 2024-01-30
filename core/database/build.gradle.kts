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
                api(libs.multiplatformSettings.test)
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
                implementation(libs.sqldelight.driver.jvm)
            }
        }
        val androidUnitTest by getting {
            dependencies { }
        }

//        // JS Sourcesets
//        val jsMain by getting {
//            dependencies {
//                implementation("app.cash.sqldelight:web-worker-driver:2.0.1")
//                implementation(devNpm("copy-webpack-plugin", "9.1.0"))
//            }
//        }
//        val jsTest by getting {
//            dependencies { }
//        }
//
//        // iOS Sourcesets
//        val iosMain by getting {
//            dependencies {
//                implementation("app.cash.sqldelight:native-driver:2.0.1")
//            }
//        }
//        val iosTest by getting {
//            dependencies { }
//        }
    }
}

sqldelight {
    databases {
        create("ScriptureNowDatabase") {
            packageName.set("com.caseyjbrooks.database")
            generateAsync.set(true)
            verifyMigrations.set(true)
            schemaOutputDirectory.set(file("src/commonMain/sqldelight/databases"))
        }
    }
}
