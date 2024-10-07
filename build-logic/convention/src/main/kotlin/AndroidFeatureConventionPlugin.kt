import com.kesicollection.convention.implementation
import com.kesicollection.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("kesihabits.android.library")
                apply("kesihabits.hilt")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            dependencies {
//                implementation(project(":core:model"))
                implementation(libs.findLibrary("hilt.navigation.compose").get())
                implementation(libs.findLibrary("androidx.navigation.compose").get())
                implementation(libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
                implementation(libs.findLibrary("androidx.lifecycle.viewModelCompose").get())
                implementation(libs.findLibrary("kotlinx.serialization.json").get())
            }
        }
    }
}