import com.kesicollection.convention.configureKotlinJvm
import com.kesicollection.convention.implementation
import com.kesicollection.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.jvm")
            }
            configureKotlinJvm()
            dependencies {
                implementation(libs.findLibrary("kotlinx.coroutines.android").get())
            }
        }
    }
}