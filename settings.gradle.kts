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

//include(":modules:models")
//include(":modules:resources")
//include(":modules:utils")

//include(":modules:config")

//include(":modules:api")
//include(":modules:database")

//include(":modules:repositories")

//include(":modules:viewmodels")
//include(":modules:notifications")
//include(":modules:widgets")
//include(":modules:jobs")
//include(":modules:ui")

//include(":modules:analytics")

//include(":shared")

include(":composeApp")
//include(":site")
