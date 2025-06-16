plugins {
    alias(libs.plugins.hugo)
}

hugo {
    version = "0.130.0"
    sourceDirectory = "src/main/hugo"
}

tasks.hugoBuild {
    outputDirectory = project.layout.buildDirectory.asFile.get().resolve("dist/hugo")
}

val clean by tasks.creating(Delete::class) {
    delete = setOf(project.layout.buildDirectory.asFile.get())
}
