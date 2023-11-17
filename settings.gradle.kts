pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

val conventionDir = "./gradle-convention-plugins"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("$conventionDir/gradle/conventionLibs.versions.toml"))
        }
    }
}

includeBuild(conventionDir)

rootProject.name = "scripture-now"

include(":core:database")
include(":core:routing")

include(":features:bible")
include(":features:prayer")
include(":features:scriptureMemory")
include(":features:topicalBible")

include(":shared")

include(":composeApp")

//include(":site")
