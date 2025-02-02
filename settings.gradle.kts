pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "kesihabits"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")
include(":feature:weeklyhabits")
include(":core:designsystem")
include(":core:model")
include(":database:impl:room")
include(":database:api")
include(":data:habit")
include(":data:weekspaging")
include(":core:redux")
include(":feature:addentry")
include(":feature:habitpicker")
include(":feature:createhabit")
include(":data:entry")
include(":feature:influencerpicker")
include(":data:influencer")
include(":feature:createinfluencer")
include(":domain:datetime")
include(":data:humanneed")
