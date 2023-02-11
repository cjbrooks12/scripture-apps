plugins {
    base
}

repositories {
    mavenCentral()
    google()
    mavenLocal()
    maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots")
}

group = Config.groupId
