plugins {
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

// Kotlin config
// ---------------------------------------------------------------------------------------------------------------------

kotlin {
    // targets
    jvm { }
    js(IR) {
        browser {
            testTask {
                enabled = false
            }
        }
    }
    ios { }

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
                api(project(":modules:router"))
                api(project(":modules:sync"))

                api(project(":modules:votd:api"))
                api(project(":modules:votd:impl"))

                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.client.serialization)
                implementation(libs.ktor.client.serialization.json)
            }
        }

        val jvmMain by getting {
            kotlin.srcDir("build/generated/ksp/jvm/jvmMain/kotlin")
            dependencies {
                implementation(libs.ktor.client.okhttp)
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(libs.ktor.client.js)
            }
        }

        val iosMain by getting {
            dependencies {
                implementation(libs.ktor.client.ios)
            }
        }
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
        "kspJvm",
        "kspJs",
        "kspIosArm64",
        "kspIosX64",
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

