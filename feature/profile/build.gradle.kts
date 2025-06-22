plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp") version "2.1.21-2.0.1"
}

android {
    namespace = "com.mleon.feature.profile"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Compose
    implementation(libs.androidx.ui) //dependencia principal de compose
    implementation(libs.androidx.ui.tooling.preview) // vista previa para compose
    implementation(libs.androidx.foundation.android) // Proporciona componentes básicos de la interfaz de usuario, como contenedores y modificadores, para Jetpack Compose.
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.activity.compose) // Proporciona integración de Jetpack Compose con actividades de Android.

    //Coil
    implementation(libs.coil.compose)
}