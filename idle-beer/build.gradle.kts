plugins {
    kotlin("jvm")
    `copper-leaf-base`
    `copper-leaf-version`
    `copper-leaf-lint`
    id("org.jetbrains.compose")
    kotlin("plugin.serialization")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(libs.ballast.core)
    implementation(libs.ballast.savedState)
    implementation(libs.ballast.debugger)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.ktor.client.okhttp)
    implementation("com.russhwolf:multiplatform-settings:0.9")
}

compose.desktop {
    application {
        mainClass = "com.copperleaf.beer.MainKt"
    }
}
