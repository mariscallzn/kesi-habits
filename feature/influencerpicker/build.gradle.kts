plugins {
    alias(libs.plugins.kesihabits.android.feature)
    alias(libs.plugins.kesihabits.android.library.compose)
}

android {
    namespace = "com.kesicollection.feature.influencerpicker"
}

dependencies {
    implementation(projects.data.influencer)
    implementation(projects.core.redux)
}