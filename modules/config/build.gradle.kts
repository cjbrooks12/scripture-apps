plugins {
    `copper-leaf-android-library`
    `copper-leaf-targets`
    `copper-leaf-base`
    `copper-leaf-version`
    `copper-leaf-testing`
    `copper-leaf-lint`
    id("com.github.gmazzo.buildconfig")
}
android {
    namespace = "com.caseyjbrooks.scripturenow.config"
}

kotlin {
    sourceSets {
        // Common Sourcesets
        val commonMain by getting {
            dependencies {
                api(project(":modules:models"))
                api(project(":modules:resources"))
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

    infix fun String.field(value: String) {
        buildConfigField("String", this, "\"$value\"")
    }

    infix fun String.field(value: Boolean) {
        buildConfigField("boolean", this, value.toString())
    }

    "APP_VERSION" field project.version.toString()
    "BASEURL_VERSEOFTHEDAYDOTCOM" field "https://www.verseoftheday.com/"
    "BASEURL_BIBLEGATEWAY" field "https://www.biblegateway.com/"
    "BASEURL_OURMANNA" field "https://beta.ourmanna.com/api/v1/"
    "BASEURL_THEYSAIDSO" field "https://quotes.rest/"

    "LOG_PREFIX" field "SN"
    "LOG_API_CALLS" field false
    "LOG_DB_QUERIES" field false
    "LOG_REPOSITORIES" field true
    "LOG_VIEWMODELS" field true
}
