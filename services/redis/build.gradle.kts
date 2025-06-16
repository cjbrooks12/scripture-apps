
val clean by tasks.registering(Delete::class) {
    delete(project.file("build"))
}
