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

        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
            authentication{
                create<BasicAuthentication>("basic")
            }
            credentials{
                username = "mapbox"
                password = "sk.eyJ1IjoidGllbjIyNTIxNDY5IiwiYSI6ImNtNDIzMHF4djE3Z3cyaXM1M2dnZWcxc3cifQ.x1-54LUkZOsuCN0WUp9CBA"
            }
        }
    }
}

rootProject.name = "search_mapbox"
include(":app")
 