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
//                api(project(":modules:utils"))
//                api(project(":modules:models"))
//
//                api(project(":modules:config"))
//
//                api(project(":modules:api"))
//                api(project(":modules:database"))
//                api(project(":modules:repositories"))
//
//                api(project(":modules:viewmodels"))
//                api(project(":modules:ui"))
//                api(project(":modules:notifications"))
//                api(project(":modules:widgets"))
//                api(project(":modules:analytics"))
//                api(project(":modules:jobs"))
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
