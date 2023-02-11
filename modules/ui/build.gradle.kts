plugins {
    `copper-leaf-android-library`
    `copper-leaf-targets`
    `copper-leaf-base`
    `copper-leaf-version`
    `copper-leaf-testing`
    `copper-leaf-lint`
    `copper-leaf-compose`
}

kotlin {
    sourceSets {
        all {
            languageSettings.apply {
                optIn("androidx.compose.material3.ExperimentalMaterial3Api")
            }
        }

        // Common Sourcesets
        val commonMain by getting {
            dependencies {
                api(project(":modules:viewmodels"))
                api(libs.ballast.navigation)

                api(libs.android.compose.icons)
                implementation(libs.android.compose.accompanist.systemuicontroller)
                implementation(libs.android.compose.accompanist.swiperefresh)
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
                implementation(libs.coil.core)
                implementation(libs.coil.compose)

                implementation(project.dependencies.platform(libs.firebase.bom))
                implementation(libs.firebase.auth.ui)
                api(libs.android.playStore.review)
            }
        }
        val androidUnitTest by getting {
            dependencies { }
        }
    }
}
