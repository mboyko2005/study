plugins {
    alias(libs.plugins.android.application) // Плагин для Android
}

android {
    namespace = "com.example.study"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.study"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // FastAdapter
    implementation("com.mikepenz:fastadapter:5.7.0")
    implementation("com.mikepenz:fastadapter-extensions-binding:5.7.0")
    implementation ("androidx.core:core:1.9.0")

    // Room
    implementation("androidx.room:room-runtime:2.5.2")
    annotationProcessor("androidx.room:room-compiler:2.5.2")

    implementation ("androidx.recyclerview:recyclerview:1.3.1")

    // Testing Room
    testImplementation("androidx.room:room-testing:2.5.2")
}
