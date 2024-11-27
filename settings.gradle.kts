pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }

        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }

        maven {

            url = uri("https://maven.pkg.github.com/pesapalsnm/androidPaymentSDK")
            credentials {

                username = "TO_BE_PROVIDED_BY_SUPPORT"
                password = "TO_BE_PROVIDED_BY_SUPPORT"

            }
        }


    }
}

rootProject.name = "PesapalAPI3Demo"
include(":app")
 