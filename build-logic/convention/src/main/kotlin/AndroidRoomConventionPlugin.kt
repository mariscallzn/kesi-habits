import com.google.devtools.ksp.gradle.KspExtension
import com.kesicollection.convention.implementation
import com.kesicollection.convention.ksp
import com.kesicollection.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidRoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("androidx.room")
                apply("com.google.devtools.ksp")
            }

            extensions.configure<KspExtension> {
                arg("room.generateKotlin", "true")
            }

//            extensions.configure<RoomExtension> {
//                // The schemas directory contains a schema file for each version of the Room database.
//                // This is required to enable Room auto migrations.
//                // See https://developer.android.com/reference/kotlin/androidx/room/AutoMigration.
//                schemaDirectory("$projectDir/schemas")
//            }

            dependencies {
                implementation(libs.findLibrary("room.runtime").get())
                implementation(libs.findLibrary("room.ktx").get())
                ksp(libs.findLibrary("room.compiler").get())
            }
        }
    }
}