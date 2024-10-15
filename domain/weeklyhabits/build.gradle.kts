plugins {
    alias(libs.plugins.kesihabits.android.library)
    alias(libs.plugins.kesihabits.hilt)
}

android {
    namespace = "com.kesicollection.domain.weeklyhabits"
}

dependencies {
    api(projects.core.model)
}