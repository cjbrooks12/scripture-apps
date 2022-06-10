plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    `copper-leaf-base`
    `copper-leaf-version`
    `copper-leaf-lint`
    id("com.google.devtools.ksp")
    id("com.squareup.sqldelight")
    id("com.github.gmazzo.buildconfig")
}

var projectVersion: ProjectVersion by project.extra
description = "Opinionated Application State Management framework for Kotlin Multiplatform"

android {
    compileSdk = 31
    defaultConfig {
        minSdk = 28
        targetSdk = 31
    }

    sourceSets {
        getByName("main") {
            setRoot("src/androidMain")
        }
        getByName("androidTest") {
            setRoot("src/androidTest")
        }
    }
}

// Kotlin config
// ---------------------------------------------------------------------------------------------------------------------

kotlin {
    // targets
    android {  }
//    jvm { }
//    js(IR) {
//        browser {
//            testTask {
//                enabled = false
//            }
//        }
//    }
//    ios { }

    // sourcesets
    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlin.time.ExperimentalTime")
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
            }
        }

        // Common Sourcesets
        val commonMain by getting {
            dependencies {
                api(project(":modules:common"))

                api(libs.ballast.core)
                api(libs.ballast.savedState)
                api(libs.ballast.repository)
                api(libs.ballast.navigation)
                api(libs.ballast.debugger)

                api(libs.kotlinx.serialization.json)

                api(libs.ksp.ktorfit.runtime)
                api(libs.sqldelight.coroutines)

                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.client.serialization)
                implementation(libs.ktor.client.serialization.json)

                api(libs.benasher44.uuid)
                api(libs.kermit.core)
            }
        }

        val androidMain by getting {
            kotlin.srcDir("build/generated/ksp/jvm/androidMain/kotlin")
            dependencies {
                implementation(libs.ktor.client.okhttp)
                implementation(libs.sqldelight.driver.android)

                implementation(project.dependencies.platform(libs.firebase.bom))
                implementation(libs.firebase.auth)
            }
        }

//        val jvmMain by getting {
//            kotlin.srcDir("build/generated/ksp/jvm/jvmMain/kotlin")
//            dependencies {
//                implementation(libs.ktor.client.okhttp)
//            }
//        }
//
//        val jsMain by getting {
//            kotlin.srcDir("build/generated/ksp/jvm/jsMain/kotlin")
//            dependencies {
//                implementation(libs.ktor.client.js)
//            }
//        }
//
//        val iosMain by getting {
//            kotlin.srcDir("build/generated/ksp/jvm/iosMain/kotlin")
//            dependencies {
//                implementation(libs.ktor.client.ios)
//            }
//        }
    }
}

tasks.withType<JavaCompile> {
    sourceCompatibility = Config.javaVersion
    targetCompatibility = Config.javaVersion
}
tasks.withType<Test> {
    testLogging {
        showStandardStreams = true
    }
}

// BuildConfig
// ---------------------------------------------------------------------------------------------------------------------

buildConfig {
    useKotlinOutput {
        internalVisibility = true
        topLevelConstants = true
    }

    buildConfigField("String", "BASE_URL_OURMANNA", "\"https://beta.ourmanna.com/api/v1/\"")
}

// SqlDelight
// ---------------------------------------------------------------------------------------------------------------------

sqldelight {
    database("ScriptureNowDatabase") {
        packageName = "com.copperleaf.scripturenow"
    }
}

// KSP
// ---------------------------------------------------------------------------------------------------------------------

dependencies {
    val kspTargets = listOf(
        "kspMetadata",
        "kspAndroid",
//        "kspJvm",
//        "kspJs",
//        "kspIosArm64",
//        "kspIosX64",
    )
    val kspLibs = listOf(
        libs.ksp.ktorfit.compiler,
    )

    kspLibs.forEach { kspLib ->
        kspTargets.forEach { kspTarget ->
            add(kspTarget, kspLib)
        }
    }
}
