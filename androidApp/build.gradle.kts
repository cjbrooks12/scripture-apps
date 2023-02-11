@file:Suppress("UnstableApiUsage")
import com.google.firebase.appdistribution.gradle.AppDistributionExtension

plugins {
    `copper-leaf-android-app`
    `copper-leaf-base`
    `copper-leaf-version`
    `copper-leaf-lint`

    id("com.google.gms.google-services")
    id("com.google.firebase.appdistribution")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
}

android {
    defaultConfig {
        val projectVersion = Config.projectVersion(project)
        versionName = projectVersion.projectVersion
        versionCode = projectVersion.projectVersionInt
    }

    buildFeatures {
        compose = true
    }

    signingConfigs {
        val publishConfiguration = Config.publishConfiguration(project)
        getByName("debug") {

        }
        create("scriptureMemoryRelease") {
            storeFile = file("../release.keystore")
            storePassword = publishConfiguration.keystorePassword
            keyAlias = publishConfiguration.scriptureMemoryKeyAlias
            keyPassword = publishConfiguration.scriptureMemoryKeyPassword
        }
        create("topicalBibleRelease") {
            storeFile = file("../release.keystore")
            storePassword = publishConfiguration.keystorePassword
            keyAlias = publishConfiguration.openBibleKeyAlias
            keyPassword = publishConfiguration.openBibleKeyPassword
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debug")
        }

        release {
            // Releases are signed by CI/CD pipelines

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
                "proguard-rules.pro"
            )
        }
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0"
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

dependencies {
    implementation(project(":shared"))

    implementation(libs.androidx.splashscreen)
    implementation(libs.androidx.compose.activity)
    implementation("io.ktor:ktor-client-logging-jvm:2.2.3")
}
