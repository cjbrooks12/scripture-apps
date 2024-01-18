pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

val conventionDir = "./../../copperleaf/gradle-convention-plugins"

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
include(":core:domain")
include(":core:di")
include(":core:routing")

include(":data:prayer1")
include(":domain:prayer2")
include(":features:prayer3:schedules")
include(":features:prayer3:screens")
include(":features:prayer3:widgets")
include(":pillars:prayer4")

include(":features:bible")
include(":features:foryou")
include(":features:scriptureMemory")
include(":features:settings")
include(":features:topicalBible")

include(":shared")

include(":composeApp")
