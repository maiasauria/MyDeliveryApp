plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.dagger.hilt.android") version "2.56.2"
    id("com.google.devtools.ksp") version "2.1.21-2.0.1"
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
    // Kotlin Stdlib & Core KTX
    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.core.ktx)

//    implementation(libs.androidx.lifecycle.runtime.ktx)
//    implementation(libs.androidx.activity.compose)
//    implementation(platform(libs.androidx.compose.bom)) // Dependencia que se usa para manejar versiones de Compose
//    implementation(libs.androidx.ui)
//    //implementation(libs.androidx.ui.graphics) // Dependencia para gráficos en Compose
//    implementation(libs.androidx.ui.tooling.preview) // Dependencia para previsualizar en Android Studio - En uso
    implementation(libs.androidx.material3) // OK se usa Material Design 3 en Compose
    implementation(libs.material.icons.extended) // OK Se usa Para iconos extendidos de Material Design en Compose
    implementation(libs.androidx.ui.text.google.fonts) //OK se usa, Habilita uso de tipografías de Google Fonts en Compose
//
//    //Compose
//    implementation(libs.androidx.lifecycle.viewmodel.compose)

//
//    // Retrofit
//    implementation(libs.retrofit)
//    implementation(libs.converter.gson)
//    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    // Room (runtime, compiler, ktx)
    // implementation(libs.androidx.room.runtime)
    // ksp(libs.androidx.room.compiler)
    // implementation(libs.androidx.room.ktx)

    // implementation(libs.androidx.navigation.compose.android)

    // ViewModel & LiveData (ktx)
    // implementation(libs.androidx.lifecycle.viewmodel.ktx)
    // implementation(libs.androidx.lifecycle.livedata.ktx)

    // Navigation (fragment & ui)
    // implementation(libs.androidx.navigation.fragment.ktx)
    // implementation(libs.androidx.navigation.ui.ktx)

    // Material Components
    // implementation(libs.material)

    // ConstraintLayout
    // implementation(libs.androidx.constraintlayout)

    // Hilt (DI)
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.runtime.android)

    implementation(libs.androidx.appcompat)

    // Coil
    // implementation(libs.coil.compose)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.activity)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(project(":core:model"))
    implementation(project(":core:data"))
    implementation(project(":core:navigation"))
    implementation(project(":utils"))
    implementation(project(":feature:cart"))
    implementation(project(":feature:profile"))

}
