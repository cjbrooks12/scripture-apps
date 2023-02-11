plugins {
    `copper-leaf-android-library`
    `copper-leaf-targets`
    `copper-leaf-base`
    `copper-leaf-version`
    `copper-leaf-testing`
    `copper-leaf-lint`
    id("com.github.gmazzo.buildconfig")
}

kotlin {
    sourceSets {
        // Common Sourcesets
        val commonMain by getting {
            dependencies {
                api(project(":modules:utils"))
            }
        }
        val commonTest by getting {
            dependencies { }
        }

        // plain JVM Sourcesets
        val jvmMain by getting {
            dependencies {
                implementation(project.dependencies.platform(libs.firebase.bom))
                implementation(libs.firebase.remoteConfig)
            }
        }
        val jvmTest by getting {
            dependencies { }
        }

        // Android JVM Sourcesets
        val androidMain by getting {
            dependencies {
                implementation(project.dependencies.platform(libs.firebase.bom))
                implementation(libs.firebase.remoteConfig)
            }
        }
        val androidUnitTest by getting {
            dependencies { }
        }
    }
}

buildConfig {
    useKotlinOutput {
        internalVisibility = true
        topLevelConstants = true
    }

    buildConfigField("String", "BASEURL_VERSEOFTHEDAYDOTCOM", "\"https://www.verseoftheday.com/\"")
    buildConfigField("String", "BASEURL_BIBLEGATEWAY", "\"https://www.biblegateway.com/\"")
    buildConfigField("String", "BASEURL_OURMANNA", "\"https://beta.ourmanna.com/api/v1/\"")
    buildConfigField("String", "BASEURL_THEYSAIDSO", "\"https://quotes.rest/\"")

    buildConfigField("String", "LOG_PREFIX", "\"SN\"")
    buildConfigField("boolean", "LOG_API_CALLS", "false")
    buildConfigField("boolean", "LOG_DB_QUERIES", "false")
    buildConfigField("boolean", "LOG_REPOSITORIES", "true")
    buildConfigField("boolean", "LOG_VIEWMODELS", "true")
}
