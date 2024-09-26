import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

fun getApiKey(key: String): String = gradleLocalProperties(rootDir, providers).getProperty(key)
val properties = Properties().apply {
    load(FileInputStream(rootProject.file("local.properties")))
}

android {
    namespace = "com.example.everymoment"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.everymoment"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "API_KEY", "\"${getApiKey("API_KEY")}\"")
        buildConfigField("String", "KAKAO_NATIVE_KEY", "\"${getApiKey("KAKAO_NATIVE_KEY")}\"")
    }
    buildTypes {
        debug {
            isMinifyEnabled = false
            manifestPlaceholders["KAKAO_NATIVE_KEY"] = properties["KAKAO_NATIVE_KEY"] as String
        }
        release {
            isMinifyEnabled = false
            manifestPlaceholders["KAKAO_NATIVE_KEY"] = properties["KAKAO_NATIVE_KEY"] as String
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.okhttp)
    implementation(libs.play.services.location)
    implementation(libs.androidx.work.runtime.ktx)
    implementation("com.github.prolificinteractive:material-calendarview:2.0.1")
    implementation ("com.jakewharton.threetenabp:threetenabp:1.2.1")
    implementation ("com.google.android.material:material:1.2.0")
    implementation ("com.kakao.sdk:v2-all:2.20.6")
    implementation ("com.kakao.sdk:v2-user:2.20.6")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}