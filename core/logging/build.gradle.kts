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
                api(libs.kermit)
                api(libs.kotlinx.datetime)
                api(libs.kotlinx.coroutines.core)
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
