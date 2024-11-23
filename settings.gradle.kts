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
        maven { url =uri("https://www.jitpack.io")

        }
        maven { url=uri("https://maven.pkg.github.com/spotify/android-auth") }  // For Auth SDK
        maven { url=uri("https://maven.pkg.github.com/spotify/android-sdk") }   // For App Remote SDK


    }
}

rootProject.name = "MusicPlayer"
include(":app")
 