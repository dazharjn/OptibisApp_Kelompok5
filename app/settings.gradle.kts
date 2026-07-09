pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        id("com.android.application") version "9.1.1" apply false
        id("org.jetbrains.kotlin.android") version "2.2.10" apply false
        id("com.google.gms.google-services") version "4.4.1" apply false
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "optibis.id"
include(":app")