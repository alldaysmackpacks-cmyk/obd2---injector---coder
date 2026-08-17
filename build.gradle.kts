plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "au.com.fuelcoder"
    compileSdk = 34

    defaultConfig {
        applicationId = "au.com.fuelcoder"
        minSdk = 26
        targetSdk = 34
        versionCode = 2
        versionName = "1.0-ranger"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    sourceSets["main"].manifest.srcFile("AndroidManifest.xml")
    sourceSets["main"].java.srcDirs(".", "core/src/main/kotlin")
    sourceSets["main"].res.srcDir("src/main/res")

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.fragment:fragment-ktx:1.8.2")
}
