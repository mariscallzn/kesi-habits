plugins {
    alias(libs.plugins.kesihabits.android.library)
    alias(libs.plugins.kesihabits.android.room)
    alias(libs.plugins.kesihabits.hilt)
}

android {
    namespace = "com.kesicollection.database.impl.room"
}

dependencies {
    api(projects.database.api)
}