@file:Suppress("UnstableApiUsage")

import com.copperleaf.gradle.ConventionConfig
import com.google.firebase.appdistribution.gradle.AppDistributionExtension
import java.time.LocalDateTime

plugins {
    alias(libs.plugins.kotlinx.serialization)
}

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
        minSdk = 31

        val projectVersion = ConventionConfig.projectVersion(project)
        versionName = projectVersion.projectVersion

        val now = LocalDateTime.now()
        val year = "${now.year.toString().takeLast(2)}".padStart(3, '0')
        val day = "${now.dayOfYear}".padStart(3, '0')
        val hour = "${now.hour}".padStart(2, '0')
        val minute = "${now.minute}".padStart(2, '0')
        versionCode = "$year$day$hour$minute".toInt()
    }

    bundle {
        language {
            enableSplit = true
        }
        density {
            enableSplit = true
        }
        abi {
            enableSplit = true
        }
    }

    signingConfigs {
        val publishConfiguration = ConventionConfig.publishConfig(project)
        getByName("debug") {
        }
        create("release") {
            storeFile = file(publishConfiguration.androidKeystorePath)
            storePassword = publishConfiguration.androidKeystorePassword
            keyAlias = "scripturememory_keystore"
            keyPassword = publishConfiguration.androidKeystoreKeyPassword
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debug")
            isDebuggable = true
        }

        release {
            // Enables code shrinking, obfuscation, and optimization for only
            // your project's release build type.
            isMinifyEnabled = true

            // Enables resource shrinking, which is performed by the
            // Android Gradle plugin.
            isShrinkResources = true

            // TODO: remove this line before actually going live with this app!
            isDebuggable = true

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
        create("local") {
            dimension = "mode"
            applicationId = "com.caseybrooks.scripturememory.local"

            signingConfig = signingConfigs.getByName("debug")
            configure<AppDistributionExtension> {
                artifactType = "APK"
                releaseNotesFile = "${project.projectDir}/releasenotes.txt"
                groups = "testers"
            }
        }
        create("qa") {
            dimension = "mode"
            applicationId = "com.caseybrooks.scripturememory.qa"

            signingConfig = signingConfigs.getByName("debug")
            configure<AppDistributionExtension> {
                artifactType = "APK"
                releaseNotesFile = "${project.projectDir}/releasenotes.txt"
                groups = "testers"
            }
        }
        create("prod") {
            dimension = "mode"
            applicationId = "com.caseybrooks.scripturememory"

            signingConfig = signingConfigs.getByName("release")
            configure<AppDistributionExtension> {
                artifactType = "AAB"
                releaseNotesFile = "${project.projectDir}/releasenotes.txt"
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
                api(project(":core:api"))
                api(project(":core:database"))
                api(project(":core:datetime"))
                api(project(":core:di"))
                api(project(":core:domain"))
                api(project(":core:logging"))
                api(project(":core:notifications"))
                api(project(":core:routing"))
                api(project(":core:ui"))

                api(project(":pillars:prayerPillar"))
                api(project(":pillars:scriptureMemoryPillar"))
                api(project(":pillars:verseOfTheDayPillar"))

                api(project(":features:bible"))
                api(project(":features:foryou"))
                api(project(":features:settings"))
                api(project(":features:topicalBible"))
                api(project(":features:debugScreens"))
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
                implementation(project.dependencies.platform("com.google.firebase:firebase-bom:32.5.0"))
                implementation("com.google.firebase:firebase-analytics")
                implementation("com.google.firebase:firebase-auth")
                implementation("com.google.firebase:firebase-firestore")
                implementation("com.google.firebase:firebase-crashlytics")
                implementation("com.google.firebase:firebase-perf")
                implementation("com.google.firebase:firebase-appdistribution:16.0.0-beta11")
            }
        }

//        val iosMain by getting {
//            dependencies {
//                implementation(libs.ktor.client.darwin)
//            }
//        }
//
//        val jsMain by getting {
//            dependencies {
//                implementation(libs.ktor.client.js)
//            }
//        }
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
