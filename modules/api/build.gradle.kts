plugins {
    `copper-leaf-android-library`
    `copper-leaf-targets`
    `copper-leaf-base`
    `copper-leaf-version`
    `copper-leaf-testing`
    `copper-leaf-lint`
    kotlin("plugin.serialization")
    id("com.google.devtools.ksp")
    id("de.jensklingenberg.ktorfit")
}

kotlin {
    sourceSets {
        all {
            languageSettings {
                optIn("kotlinx.serialization.ExperimentalSerializationApi")
                optIn("nl.adaptivity.xmlutil.ExperimentalXmlUtilApi")
            }
        }

        // Common Sourcesets
        val commonMain by getting {
            dependencies {
                api(project(":modules:config"))
                implementation(libs.ksp.ktorfit.runtime)

                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.client.serialization)
                implementation(libs.ktor.client.serialization.json)
                implementation(libs.ktor.client.serialization.xml)

                implementation(libs.jsoup)
            }
        }
        val commonTest by getting {
            dependencies { }
        }

        // plain JVM Sourcesets
        val jvmMain by getting {
            dependencies {
                api(libs.ktor.client.okhttp)
            }
        }
        val jvmTest by getting {
            dependencies { }
        }

        // Android JVM Sourcesets
        val androidMain by getting {
            dependencies {
                api(libs.ktor.client.okhttp)

                api(project.dependencies.platform(libs.firebase.bom))
                api(libs.firebase.auth)
            }
        }
        val androidUnitTest by getting {
            dependencies { }
        }
    }
}

dependencies {
    val kspTargets = listOf(
        "kspCommonMainMetadata",
        "kspAndroid",
        "kspJvm",
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
android {
    namespace = "com.caseyjbrooks.scripturenow.api"
}
