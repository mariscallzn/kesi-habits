plugins {
    alias(libs.plugins.kesihabits.android.feature)
    alias(libs.plugins.kesihabits.android.library.compose)
}

android {
    namespace = "com.kesicollection.feature.addentry"
}

dependencies {
    implementation(projects.core.redux)
    implementation(projects.data.entry)
    implementation(projects.data.humanneed)
    implementation(projects.domain.datetime)
}