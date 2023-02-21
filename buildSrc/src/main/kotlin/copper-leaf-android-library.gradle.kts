@file:Suppress("UnstableApiUsage", "UNUSED_VARIABLE")

import gradle.kotlin.dsl.accessors._7d598b3cc797789a1092079676947f3b.coreLibraryDesugaring
import org.gradle.accessors.dm.LibrariesForLibs
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    id("com.android.library")
}

android {
    compileSdk = 33
    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        val release by getting {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
    lint {
        disable += listOf("GradleDependency")
    }
    publishing {
        multipleVariants {
            withSourcesJar()
            withJavadocJar()
            allVariants()
        }
    }
}

val libs = the<LibrariesForLibs>()
dependencies {
    coreLibraryDesugaring(libs.android.desugaring)
}
