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
include(":core:di")
include(":core:routing")

include(":features:bible")
include(":features:foryou")
include(":features:prayer")
include(":features:scriptureMemory")
include(":features:settings")
include(":features:topicalBible")

include(":shared")

include(":composeApp")
