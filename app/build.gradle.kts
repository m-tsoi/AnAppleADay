
import java.util.Properties

val propertiesFile = File(rootProject.projectDir, "app/api.properties")
val apiProperties = Properties().apply {
    load(propertiesFile.inputStream())
}

// Retrieve API key
val ninjaKey: String = apiProperties.getProperty("NINJA_URL")
val ninjaUrl: String = apiProperties.getProperty("NINJA_KEY")
val edamamUrl: String = apiProperties.getProperty("EDAMAM_URL")
val edamamId: String = apiProperties.getProperty("EDAMAM_ID")
val edamamKey: String = apiProperties.getProperty("EDAMAM_KEY")

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("kotlin-kapt")
}

android {
    namespace = "com.cs125.anappleaday"
    compileSdk = 34

    buildFeatures.buildConfig = true

    defaultConfig {
        applicationId = "com.cs125.anappleaday"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "ninjaUrl", ninjaUrl)
        buildConfigField("String", "ninjaKey", ninjaKey)
        buildConfigField("String", "edamamUrl", edamamUrl)
        buildConfigField("String", "edamamId", edamamId)
        buildConfigField("String", "edamamKey", edamamKey)

    }

    sourceSets {
        // Adds exported schema location as test app assets.
        getByName("androidTest").assets.srcDir("src/main/data/schemas")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.4.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.4.1")
    implementation(kotlin("reflect"))
    implementation("nl.joery.timerangepicker:timerangepicker:1.0.0")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.7.1"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-storage")

    // API tool
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    // Kotlin Coroutine
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")

    // Health data api
    implementation("com.google.android.libraries.healthdata:health-data-api:1.0.0-alpha01")
    implementation("com.github.bumptech.glide:glide:4.16.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}