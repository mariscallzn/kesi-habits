plugins {
    alias(libs.plugins.kesihabits.jvm.library)
    alias(libs.plugins.kesihabits.hilt)
}

dependencies {
    api(projects.core.model)
}