plugins {
    alias(libs.plugins.kesihabits.android.application)
    alias(libs.plugins.kesihabits.android.application.compose)
    alias(libs.plugins.kesihabits.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.kesicollection.kesihabits"
    defaultConfig {
        applicationId = "com.kesicollection.kesihabits"
        versionCode = 1
        versionName = "1.0.0" // X.Y.Z; X = Major, Y = minor, Z = Patch level

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(projects.feature.weeklyhabits)
    implementation(projects.feature.addentry)
    implementation(projects.feature.habitpicker)
    implementation(projects.feature.createhabit)
    implementation(projects.feature.emotionpicker)
    implementation(projects.feature.createemotion)
    implementation(projects.feature.influencerpicker)
    implementation(projects.feature.createinfluencer)

    implementation(projects.core.designsystem)
    implementation(projects.database.impl.room)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    ksp(libs.hilt.compiler)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}