plugins {
    alias(libs.plugins.kesihabits.android.library)
}

android {
    namespace = "com.kesicollection.kesihabits.core.redux"
}

dependencies {
    implementation(libs.androidx.lifecycle.viewModel)
}