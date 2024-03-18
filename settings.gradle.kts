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
        jcenter()
        maven { url = uri("https://jitpack.io") }

        maven {

            url = uri("https://maven.pkg.github.com/pesapalsnm/androidPaymentSDK")
            credentials {

                username = "" // Artifactory username
                password = ""

            }
        }


    }
}

rootProject.name = "PesapalAPI3Demo"
include(":app")
 