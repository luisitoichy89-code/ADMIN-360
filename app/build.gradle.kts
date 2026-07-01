plugins {
    alias(libs.plugins.android-application)
    alias(libs.plugins.kotlin-android)
    alias(libs.plugins.compose-compiler)
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

    kotlinOptions {
        jvmTarget = "17"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.datastore)
}
