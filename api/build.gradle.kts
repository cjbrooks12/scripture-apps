plugins {
    java
    kotlin("jvm")
    `copper-leaf-base`
    `copper-leaf-version`
    `copper-leaf-lint`
    kotlin("plugin.serialization")
    application
}

dependencies {
    implementation(libs.ballast.core)
    implementation(libs.ballast.savedState)
    implementation(libs.ballast.debugger)
//    implementation(libs.kotlinx.serialization.json)
//    implementation(libs.ktor.client.okhttp)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.cio)
    implementation(libs.ktor.server.callLogging)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.authJwt)
    implementation(libs.ktor.server.serialization)
    implementation(libs.ktor.server.serialization.json)

    implementation("com.russhwolf:multiplatform-settings:0.9")
    implementation("ch.qos.logback:logback-classic:1.2.11")
}

application {
    mainClass.set("com.copperleaf.server.MainKt")
}
