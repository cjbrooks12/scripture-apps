@file:Suppress("UnstableApiUsage")

import com.copperleaf.gradle.ConventionConfig
import com.google.firebase.appdistribution.gradle.AppDistributionExtension
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnLockMismatchReport
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension

plugins {
    id("copper-leaf-base")
    id("copper-leaf-android-application")
    id("copper-leaf-targets")
    id("copper-leaf-kotest")
    id("copper-leaf-compose")
    id("copper-leaf-lint")

    id("com.google.gms.google-services")
    id("com.google.firebase.appdistribution")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
}

android {
    namespace = "com.caseyjbrooks.app"

    defaultConfig {
        minSdk = 26

        val projectVersion = ConventionConfig.projectVersion(project)
        versionName = projectVersion.projectVersion
        versionCode = projectVersion.projectVersionInt
    }

    signingConfigs {
        val publishConfiguration = ConventionConfig.publishConfig(project)
        getByName("debug") {
        }
        create("scriptureMemoryRelease") {
            storeFile = file(publishConfiguration.androidKeystorePath)
            storePassword = publishConfiguration.androidKeystorePassword
            keyAlias = "scripturememory_keystore"
            keyPassword = publishConfiguration.androidKeystoreKeyPassword
        }
        create("topicalBibleRelease") {
            storeFile = file(publishConfiguration.androidKeystorePath)
            storePassword = publishConfiguration.androidKeystorePassword
            keyAlias = "openbible_keystore"
            keyPassword = publishConfiguration.androidKeystoreKeyPassword
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debug")
        }

        release {
            // Enables code shrinking, obfuscation, and optimization for only
            // your project's release build type.
            isMinifyEnabled = true

            // Enables resource shrinking, which is performed by the
            // Android Gradle plugin.
            isShrinkResources = true

            // Includes the default ProGuard rules files that are packaged with
            // the Android Gradle plugin. To learn more, go to the section about
            // R8 configuration files.
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    flavorDimensions.add("mode")
    productFlavors {
        create("ScriptureNowDev") {
            dimension = "mode"
            applicationId = "com.caseybrooks.scripturememory.dev"

            signingConfig = signingConfigs.getByName("debug")
            configure<AppDistributionExtension> {
                artifactType = "APK"
                releaseNotesFile = "${project.projectDir}/src/ScriptureNowProd/releasenotes.txt"
                groups = "testers"
            }
        }
        create("ScriptureNowProd") {
            dimension = "mode"
            applicationId = "com.caseybrooks.scripturememory"

            signingConfig = signingConfigs.getByName("scriptureMemoryRelease")
            configure<AppDistributionExtension> {
                artifactType = "AAB"
                releaseNotesFile = "${project.projectDir}/src/ScriptureNowProd/releasenotes.txt"
                groups = "testers"
            }
        }

        create("TopicalBibleDev") {
            dimension = "mode"
            applicationId = "com.caseybrooks.openbible.dev"

            signingConfig = signingConfigs.getByName("debug")
            configure<AppDistributionExtension> {
                artifactType = "APK"
                releaseNotesFile = "${project.projectDir}/src/TopicalBibleProd/releasenotes.txt"
                groups = "testers"
            }
        }
        create("TopicalBibleProd") {
            dimension = "mode"
            applicationId = "com.caseybrooks.openbible"

            signingConfig = signingConfigs.getByName("topicalBibleRelease")
            configure<AppDistributionExtension> {
                artifactType = "AAB"
                releaseNotesFile = "${project.projectDir}/src/TopicalBibleProd/releasenotes.txt"
                groups = "testers"
            }
        }
    }
}

kotlin {
    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlin.time.ExperimentalTime")
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
                optIn("androidx.compose.material3.ExperimentalMaterial3Api")
                optIn("com.copperleaf.ballast.ExperimentalBallastApi")
            }
        }

        val commonMain by getting {
            dependencies {
                implementation(project(":shared"))
            }
        }

        val commonTest by getting {
            dependencies {
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.swing)
                implementation(libs.ktor.client.cio)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.material)
                implementation(libs.androidx.activityCompose)
                implementation("androidx.core:core-splashscreen:1.0.0-beta02")
                implementation(libs.ktor.client.cio)
                implementation(platform("com.google.firebase:firebase-bom:32.5.0"))
                implementation("com.google.firebase:firebase-analytics")
                implementation("com.google.firebase:firebase-auth")
                implementation("com.google.firebase:firebase-firestore")
                implementation("com.google.firebase:firebase-crashlytics")
                implementation("com.google.firebase:firebase-perf")
                implementation("com.google.firebase:firebase-appdistribution:16.0.0-beta11")
            }
        }

        val iosMain by getting {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(libs.ktor.client.js)
            }
        }
    }
}

// Compose Desktop config
// ---------------------------------------------------------------------------------------------------------------------

compose {
    desktop {
        application {
            mainClass = "com.caseyjbrooks.app.desktop.MainKt"
        }
    }
    experimental {
        web {
            application {
            }
        }
    }
}

rootProject.plugins.withType(org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin::class.java) {
    rootProject.the<YarnRootExtension>().yarnLockMismatchReport = YarnLockMismatchReport.NONE
    rootProject.the<YarnRootExtension>().reportNewYarnLock = false
    rootProject.the<YarnRootExtension>().yarnLockAutoReplace = true
}