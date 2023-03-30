plugins {
    `copper-leaf-android-library`
    `copper-leaf-targets`
    `copper-leaf-base`
    `copper-leaf-version`
    `copper-leaf-testing`
    `copper-leaf-lint`
    id("dev.icerock.mobile.multiplatform-resources")
}
android {
    namespace = "com.caseyjbrooks.scripturenow.resources"
}

kotlin {
    sourceSets {
        // Common Sourcesets
        val commonMain by getting {
            dependencies {
                api(libs.mokoResources.core)
            }
        }
        val commonTest by getting {
            dependencies {
                api(libs.mokoResources.test)
            }
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

multiplatformResources {
    multiplatformResourcesPackage = "com.caseyjbrooks.scripturenow.resources"
    multiplatformResourcesClassName = "ScriptureNowRes"
}
