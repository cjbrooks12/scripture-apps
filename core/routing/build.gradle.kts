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
                api(libs.ballast.core)
                api(libs.ballast.navigation)
                api(libs.benasher44.uuid)
                api(libs.koin.core)
                api("dev.chrisbanes.material3:material3-window-size-class-multiplatform:0.3.1")
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
