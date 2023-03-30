plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
}

dependencies {
    implementation("com.android.tools.build:gradle:7.4.1")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:11.0.0")
    implementation("io.kotest:kotest-framework-multiplatform-plugin-gradle:5.5.5")
    implementation("org.jetbrains.compose:compose-gradle-plugin:1.3.0")

    // Shared code generation
    implementation("com.squareup.sqldelight:gradle-plugin:1.5.5")
    implementation("com.github.gmazzo:gradle-buildconfig-plugin:3.1.0")
    implementation("dev.icerock.moko:resources-generator:0.20.1")

    // Android
    implementation("com.google.gms:google-services:4.3.15")
    implementation("com.google.firebase:firebase-appdistribution-gradle:3.2.0")
    implementation("com.google.firebase:firebase-crashlytics-gradle:2.9.2")
    implementation("com.google.firebase:perf-plugin:1.4.2")

    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}
