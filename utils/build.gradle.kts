plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

}

android {
    namespace = "com.mleon.utils"
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13"
    }
}

dependencies {
    // Kotlin Stdlib & Core KTX
    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    // Compose (using BOM)
    implementation(platform(libs.androidx.compose.bom)) // se usa para manejar versiones de Compose
    implementation(libs.androidx.ui) // Dependencia principal de Jetpack Compose
    implementation(libs.androidx.ui.tooling.preview) // Dependencia para previsualizar en Android Studio
    implementation(libs.androidx.material3) // OK se usa Material Design 3 en Compose
    implementation(libs.material.icons.extended) // OK Se usa Para iconos extendidos de Material Design en Compose
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    
    //
    implementation(libs.androidx.runtime.android) //
    implementation(libs.androidx.ui.text.android) //

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    debugImplementation(libs.androidx.ui.tooling)

    // Project Modules
    implementation(project(":core:model"))
    implementation(project(":core:navigation"))
}