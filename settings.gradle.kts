pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

//val conventionDir = "./../../copperleaf/gradle-convention-plugins"
val conventionDir = ".//gradle-convention-plugins"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("$conventionDir/gradle/conventionLibs.versions.toml"))
        }
    }
}

includeBuild(conventionDir)

rootProject.name = "scripture-now"

include(":core:api")
include(":core:ballast")
include(":core:database")
include(":core:datetime")
include(":core:domain")
include(":core:logging")
include(":core:notifications")
include(":core:di")
include(":core:routing")
include(":core:ui")

// Prayer pillar
include(":data:prayerData")
include(":domain:prayerDomain")
include(":features:prayerSchedules")
include(":features:prayerScreens")
include(":features:prayerWidgets")
include(":pillars:prayerPillar")

// Prayer pillar
include(":data:verseOfTheDayData")
include(":domain:verseOfTheDayDomain")
include(":features:verseOfTheDaySchedules")
include(":features:verseOfTheDayScreens")
include(":features:verseOfTheDayWidgets")
include(":pillars:verseOfTheDayPillar")

// Scripture Memory Pillar
include(":data:verseData")
include(":domain:verseDomain")
include(":features:scriptureMemoryScreens")
include(":pillars:scriptureMemoryPillar")

include(":features:bible")
include(":features:foryou")
include(":features:settings")
include(":features:topicalBible")
include(":features:debugScreens")

include(":abideApp")
//include(":topicalBibleApp")
