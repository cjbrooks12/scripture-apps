@file:Suppress("UnstableApiUsage")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
        mavenLocal()
    }
}

rootProject.name = "biblebits"

include(":lib:dto")
include(":lib:ktor-server-platform")

include(":services:api")
include(":services:flyway")
include(":services:keycloak")
include(":services:openfga")
include(":services:postgres")
include(":services:redis")
include(":services:site")

include(":app:composeApp")
