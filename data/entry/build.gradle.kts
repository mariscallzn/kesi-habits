plugins {
    alias(libs.plugins.kesihabits.jvm.library)
    alias(libs.plugins.kesihabits.hilt)
}

dependencies {
    api(projects.database.api)
    implementation(projects.domain.datetime)
}