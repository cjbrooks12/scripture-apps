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
                api(project(":core:routing"))
                api(project(":core:database"))
                api(project(":core:di"))
                api(project(":core:notifications"))

                api(project(":domain:prayerDomain"))

                api(libs.kotlinx.datetime)
                api(libs.ballast.core)
                api(libs.ballast.repository)
                api(libs.ballast.scheduler)
                api(libs.benasher44.uuid)
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
                implementation("androidx.work:work-runtime-ktx:2.8.1")
            }
        }
        val androidUnitTest by getting {
            dependencies { }
        }
    }
}
