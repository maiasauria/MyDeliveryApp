pluginManagement {
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

rootProject.name = "MyDeliveryApp"
include(":app")
include(":core:data")
include(":core:model")
include(":core:navigation")
include(":feature:cart")
include(":feature:checkout")
include(":feature:login")
include(":feature:orders")
include(":feature:productlist")
include(":feature:profile")
include(":feature:signup")
include(":utils")