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
include(":core:ui")

include(":data:prayer")
include(":domain:prayer2")
include(":features:prayer:schedules")
include(":features:prayer:screens")
include(":features:prayer:widgets")
include(":pillars:prayer4")

include(":features:bible")
include(":features:foryou")
include(":features:scriptureMemory")
include(":features:settings")
include(":features:topicalBible")

include(":scriptureNowApp")
include(":topicalBibleApp")
