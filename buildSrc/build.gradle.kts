plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
}

dependencies {
    // Gradle and Kotlin
    implementation("com.android.tools.build:gradle:7.0.3")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
    implementation("org.jetbrains.kotlin:kotlin-serialization:1.6.10")

    // Shared code generation
    implementation("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:1.6.10-1.0.4")
    implementation("com.squareup.sqldelight:gradle-plugin:1.5.3")
    implementation("com.github.gmazzo:gradle-buildconfig-plugin:3.0.3")

    // Android
    implementation("com.google.gms:google-services:4.3.10")
    implementation("com.google.firebase:firebase-appdistribution-gradle:3.0.1")
    implementation("com.google.firebase:firebase-crashlytics-gradle:2.8.1")
    implementation("com.google.firebase:perf-plugin:1.4.1")
//    implementation("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.0-beta01")

    // Compose
    implementation("org.jetbrains.compose:compose-gradle-plugin:1.1.0")
}
