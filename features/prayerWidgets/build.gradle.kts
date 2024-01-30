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
                api(project(":core:ballast"))
                api(project(":core:routing"))
                api(project(":core:ui"))

                api(project(":domain:prayerDomain"))
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
            dependencies {
                implementation("androidx.glance:glance-appwidget:1.0.0")
                implementation("androidx.glance:glance-material3:1.0.0")
            }
        }
        val androidUnitTest by getting {
            dependencies { }
        }
    }
}
