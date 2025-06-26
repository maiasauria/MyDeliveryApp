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
include(":feature:cart")
include(":core:model")
include(":core:data")
include(":utils")
include(":feature:profile")
include(":core:navigation")
include(":feature:checkout")
include(":feature:orders")
include(":feature:login")
include(":feature:signup")
include(":feature:productlist")
