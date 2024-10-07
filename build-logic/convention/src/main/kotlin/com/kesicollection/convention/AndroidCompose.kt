package com.kesicollection.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

/**
 * Configure Compose-specific options
 */
internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }
    }

    dependencies {
        val bom = libs.findLibrary("androidx-compose-bom").get()
        implementation(platform(bom))
        implementation(libs.findLibrary("androidx-compose-ui-tooling-preview").get())
        debugImplementation(libs.findLibrary("androidx-compose-ui-tooling").get())
    }

    extensions.configure<ComposeCompilerGradlePluginExtension> {
        stabilityConfigurationFile.set(
            rootProject.layout.projectDirectory.file("compose_compiler_config.conf")
        )
    }
}