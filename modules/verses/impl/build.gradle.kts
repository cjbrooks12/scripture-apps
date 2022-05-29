plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    `copper-leaf-base`
    `copper-leaf-version`
    `copper-leaf-lint`
    id("com.google.devtools.ksp")
    id("com.squareup.sqldelight")
}

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
                api(project(":modules:verses:api"))

                api(libs.kotlinx.serialization.json)
                api(libs.ksp.ktorfit.runtime)
                api(libs.sqldelight.coroutines)
            }
        }

        val jvmMain by getting {
            kotlin.srcDir("build/generated/ksp/jvm/jvmMain/kotlin")
            dependencies {
            }
        }

        val jsMain by getting {
            dependencies {
            }
        }

        val iosMain by getting {
            dependencies {
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

// SqlDelight
// ---------------------------------------------------------------------------------------------------------------------

sqldelight {
    database("ScriptureNowDatabase") {
        packageName = "com.copperleaf.scripturenow"
    }
}
