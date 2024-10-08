plugins {
    alias(libs.plugins.kesihabits.android.feature)
    alias(libs.plugins.kesihabits.android.library.compose)
}

android {
    namespace = "com.kesicollection.feature.weeklyhabits"
}

dependencies {
    implementation(projects.data.habit)
}