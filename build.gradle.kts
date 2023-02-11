plugins {
    `copper-leaf-base`
    `copper-leaf-version`
    kotlin("plugin.serialization") version "1.8.0" apply false
    id("com.google.devtools.ksp") version "1.8.0-1.0.8" apply false
    id("de.jensklingenberg.ktorfit") version "1.0.0" apply false
}
