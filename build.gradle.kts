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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    // The protocol core is pure Kotlin and lives outside the app module so it
    // can be compiled and unit-tested on the JVM (see core/run_tests.sh).
    sourceSets["main"].java.srcDirs("src/main/java", "../core/src/main/kotlin")
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.fragment:fragment-ktx:1.8.2")
    // org.json is provided by the Android platform (no dependency needed)
}
