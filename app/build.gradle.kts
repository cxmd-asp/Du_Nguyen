plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.navigation.safeargs.kotlin)
    alias(libs.plugins.hilt)
}

kotlin {
    jvmToolchain(17)
}

android {
    namespace = "com.example.safarione"
    compileSdk = libs.versions.compileSdk.get().toInt()

    buildFeatures {
        dataBinding = true
    }

    defaultConfig {
        applicationId = "com.example.safarione"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    //AndroidX Core
    implementation(libs.androidx.core.ktx)

    //Coroutine utilities
    implementation(libs.jetbrains.kotlinx.coroutines.core)
    implementation(libs.jetbrains.kotlinx.coroutines.android)

    //App compat
    implementation(libs.appcompat)

    //Core Ui
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview)
    implementation(libs.material)

    //Navigation component
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    implementation(libs.navigation.dynamic.features.fragment)

    // ViewModel and LiveData
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.viewmodel.savedstate)
    implementation(libs.lifecycle.common.java8)

    //Dependencies Injection
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    //Logger
    implementation(libs.timber)
    
    //Chat client
    implementation(libs.smack.android)
    implementation(libs.smack.android.extensions)
    implementation(libs.smack.im)
    implementation(libs.smack.tcp)

    // Jetpack Paging 3
    implementation(libs.paging.runtime.ktx)

    // Local database
    implementation(libs.room.ktx)
    implementation(libs.room.paging)
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)

    //DataStore
    implementation(libs.datastore)
    implementation(libs.datastore.preferences)

    implementation(libs.circleimageview)
}


configurations {
    all {
        exclude(group = "xpp3", module = "xpp3")
        exclude(group = "xpp3", module = "xpp3_min")
    }
}