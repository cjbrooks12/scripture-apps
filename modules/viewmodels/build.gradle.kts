plugins {
    `copper-leaf-android-library`
    `copper-leaf-targets`
    `copper-leaf-base`
    `copper-leaf-version`
    `copper-leaf-testing`
    `copper-leaf-lint`
}

kotlin {
    sourceSets {
        // Common Sourcesets
        val commonMain by getting {
            dependencies {
                api(project(":modules:repositories"))
                api(libs.jsonForms.core)
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