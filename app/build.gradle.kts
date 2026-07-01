plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.admin360"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.admin360"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "SUPABASE_URL", "\"${System.getenv("SUPABASE_URL") ?: "https://duspeazziwxptcrignju.supabase.co"}\"")
        buildConfigField("String", "SUPABASE_KEY", "\"${System.getenv("SUPABASE_KEY") ?: "sb_publishable_CGLNTn602vd77fEsR7yUYg_3f7eeQVu"}\"")
    }

    flavorDimensions += "version"

    productFlavors {
        create("admin") {
            dimension = "version"
            applicationId = "com.gestor360.admin"
            versionName = "1.0-admin"
        }

        create("client") {
            dimension = "version"
            applicationId = "com.gestor360.client"
            versionName = "1.0-client"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation(platform("androidx.compose:compose-bom:2024.10.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.datastore:datastore-preferences:1.1.1")
}
