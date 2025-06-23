plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.android.hilt)
}

android {
    namespace = "com.mleon.mydeliveryapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.mleon.mydeliveryapp"
        minSdk = 27
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
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13"
    }
}

dependencies {
    // Kotlin & Core KTX
    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    // Compose (using BOM)
    implementation(platform(libs.androidx.compose.bom)) // se usa para manejar versiones de Compose
    implementation(libs.androidx.ui) // Dependencia principal de Jetpack Compose
    implementation(libs.androidx.ui.tooling.preview) // Dependencia para previsualizar en Android Studio
    implementation(libs.androidx.material3) // OK se usa Material Design 3 en Compose
    implementation(libs.material.icons.extended) // OK Se usa Para iconos extendidos de Material Design en Compose
    implementation(libs.androidx.ui.text.google.fonts) //OK se usa, Habilita uso de tipografías de Google Fonts en Compose
    implementation(libs.androidx.foundation.android) //  Proporciona componentes de diseño y utilidades de Compose
    implementation(libs.androidx.activity) // Activity Compose dependency for Jetpack Compose

    // Hilt (DI)
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    //Navigation
    //implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.navigation.runtime.android)

    // Coil (image loading)
    // implementation(libs.coil.compose)

    // Room (runtime, compiler, ktx)
    // implementation(libs.androidx.room.runtime)
    // ksp(libs.androidx.room.compiler)
    // implementation(libs.androidx.room.ktx)

    //Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling) //
    debugImplementation(libs.androidx.ui.test.manifest)

    //Project Modules
    implementation(project(":core:model"))
    implementation(project(":core:data"))
    implementation(project(":core:navigation"))
    implementation(project(":utils"))
    implementation(project(":feature:cart"))
    implementation(project(":feature:profile"))
    implementation(project(":feature:checkout"))
    implementation(project(":feature:orders"))

}
