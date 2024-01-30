plugins {
    id("copper-leaf-base")
    id("copper-leaf-android-library")
    id("copper-leaf-targets")
    id("copper-leaf-kotest")
    id("copper-leaf-compose")
    id("copper-leaf-lint")
}

kotlin {
    sourceSets {
        // Common Sourcesets
        val commonMain by getting {
            dependencies {
                api(project(":core:di"))
                api(project(":core:logging"))
                api(libs.ktor.client.core)
                implementation(libs.ktor.client.logging)
                api(libs.ktor.client.serialization)
                implementation(libs.ktor.client.serialization.json)
            }
        }
        val commonTest by getting {
            dependencies { }
        }

        // plain JVM Sourcesets
        val jvmMain by getting {
            dependencies {
                api(libs.ktor.client.okhttp)
            }
        }
        val jvmTest by getting {
            dependencies { }
        }

        // Android JVM Sourcesets
        val androidMain by getting {
            dependencies {
                api(libs.ktor.client.okhttp)
            }
        }
        val androidUnitTest by getting {
            dependencies { }
        }

//        // JS Sourcesets
//        val jsMain by getting {
//            dependencies {
//                api(libs.ktor.client.js)
//            }
//        }
//        val jsTest by getting {
//            dependencies { }
//        }
//
//        // iOS Sourcesets
//        val iosMain by getting {
//            dependencies {
//                api(libs.ktor.client.darwin)
//            }
//        }
//        val iosTest by getting {
//            dependencies { }
//        }
    }
}
