pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    plugins {
        id("org.jetbrains.kotlin.android") version "2.1.21"
        id("org.jetbrains.kotlin.plugin.serialization") version "2.1.21"
        id("com.android.application") version "8.8.2"
        id("com.android.library") version "8.8.2"
        id("com.google.devtools.ksp") version "2.1.21-1.0.20"
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Gestor360-ADMIN"
include(":app")
