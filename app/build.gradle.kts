plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.ewida.mealmaster"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ewida.mealmaster"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        dataBinding = true
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.play.services.auth)

    //Splash Screen
    implementation(libs.core.splashscreen)

    //lottie
    implementation (libs.lottie)

    //Dots Indicator
    implementation(libs.dotsindicator)

    //Shimmer effect
    implementation (libs.shimmer)

    //Retrofit
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.adapter.rxjava3)
    implementation (libs.okhttp)
    implementation (libs.logging.interceptor)

    //RXJava
    implementation (libs.rxandroid)
    implementation (libs.rxjava)

    //Glide
    implementation (libs.glide)
}