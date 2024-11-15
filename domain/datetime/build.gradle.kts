plugins {
    alias(libs.plugins.kesihabits.jvm.library)
    alias(libs.plugins.kesihabits.hilt)
}

dependencies {
    implementation(projects.core.model)
}