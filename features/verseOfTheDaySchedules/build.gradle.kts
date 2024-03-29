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
                api(project(":core:notifications"))

                api(project(":domain:verseOfTheDayDomain"))
                api(libs.ballast.schedules)
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
