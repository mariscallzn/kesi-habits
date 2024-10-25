plugins {
    alias(libs.plugins.kesihabits.android.feature)
    alias(libs.plugins.kesihabits.android.library.compose)
}

android {
    namespace = "com.kesicollection.feature.habitpicker"
}

dependencies {
    implementation(projects.core.redux)
    implementation(projects.data.habit)
}