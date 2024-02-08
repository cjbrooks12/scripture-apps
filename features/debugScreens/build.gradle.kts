import com.copperleaf.gradle.ProjectVersion
import com.copperleaf.gradle.stringField

plugins {
    id("copper-leaf-base")
    id("copper-leaf-android-library")
    id("copper-leaf-targets")
    id("copper-leaf-kotest")
    id("copper-leaf-compose")
//    id("copper-leaf-lint")
    id("copper-leaf-buildConfig")
}

kotlin {
    sourceSets {
        // Common Sourcesets
        val commonMain by getting {
            dependencies {
                api(project(":core:ballast"))
                api(project(":core:routing"))
                api(project(":core:ui"))

                api(libs.ballast.schedules)
            }
        }
        val commonTest by getting {
            dependencies { }
        }

        // plain JVM Sourcesets
        val jvmMain by getting {
            dependencies { }
        }
        val jvmTest by getting {
            dependencies { }
        }

        // Android JVM Sourcesets
        val androidMain by getting {
            dependencies { }
        }
        val androidUnitTest by getting {
            dependencies { }
        }
    }
}

val projectVersion: ProjectVersion = project.version as ProjectVersion

buildConfig {
    stringField("GIT_SHA", projectVersion.latestSha)
    stringField("APP_VERSION", projectVersion.projectVersion)
}
