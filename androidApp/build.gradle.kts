@file:Suppress("UnstableApiUsage")
import com.google.firebase.appdistribution.gradle.AppDistributionExtension

plugins {
    id("com.android.application")
    id("kotlin-android")
    `copper-leaf-base`
    `copper-leaf-version`
    `copper-leaf-lint`

//    id("androidx.navigation.safeargs.kotlin")

    id("com.google.gms.google-services")
    id("com.google.firebase.appdistribution")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
}

android {
    compileSdk = 31
    defaultConfig {
        minSdk = 28
        targetSdk = 31

        versionName = "1.0.0"
//        versionCode = System.currentTimeMillis()
        versionCode = 1

        multiDexEnabled = true

        ndk.abiFilters.addAll(listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64"))
    }

    buildFeatures {
        compose = true
    }

    packagingOptions {
        exclude("META-INF/LICENSE")
        exclude("META-INF/NOTICE")
        exclude("META-INF/LICENSE.txt")
    }

    signingConfigs {
        val publishConfiguration = Config.publishConfiguration(project)
        println(publishConfiguration.debug())

        val keystoreFile = file("${project.rootDir.absolutePath}/release.keystore")
        println("path to project: ${project.rootDir.absolutePath}")
        println("path to keystore: ${keystoreFile.absolutePath} (exists=${keystoreFile.exists()}, isFile=${keystoreFile.isFile})")
        println("keystore metrics: size=${keystoreFile.length()}, sha=${HashUtils.getCheckSumFromFile(keystoreFile)}")

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

            // TODO: Figure out how to fix errors when this is enabled
            // TODO: Create process for de-obfuscation of stacktraces when logged to crash reporting / diagnostic tools
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    lint {
        // Check for errors in release builds,
        // but continue the build even when errors are found:
        isAbortOnError = false
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"

        freeCompilerArgs = listOf(
            "-Xopt-in=kotlin.RequiresOptIn"
        )
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.1"
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

    implementation(libs.androidx.core)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.compose.activity)

    implementation(libs.ktor.client.okhttp)

    implementation(libs.android.compose.material)
    implementation(libs.android.compose.previews)

    implementation(libs.sqldelight.driver.android)

    // Desugaring
    coreLibraryDesugaring(libs.android.desugaring)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.performanceMonitoring)
    implementation(libs.firebase.remoteConfig)
    implementation(libs.firebase.cloudMessaging)
    implementation(libs.firebase.inAppMessaging)
}
