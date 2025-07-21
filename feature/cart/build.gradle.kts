plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.android.hilt)
    alias(libs.plugins.kotlin.ksp)
    id("io.gitlab.arturbosch.detekt") version("1.23.8")
}

android {
    namespace = "com.mleon.feature.cart"
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
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10" // Or the latest compatible version
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation("androidx.compose.runtime:runtime:1.5.4")
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.media3.common.ktx) // Biblioteca de AndroidX Media3 para manejar medios, como audio y video, en aplicaciones Android. Proporciona una API unificada para reproducir contenido multimedia.

    //Material Design
    implementation(libs.androidx.material3.android)
    implementation(libs.material.icons.extended)

    //Compose
    implementation(libs.androidx.ui) //dependencia principal de compose
    implementation(libs.androidx.ui.tooling.preview) // vista previa para compose
    implementation(libs.androidx.hilt.navigation.compose) // Integración de Hilt con Jetpack Compose para la inyección de dependencias en composables.
    implementation(libs.androidx.foundation.android) // Proporciona componentes básicos de la interfaz de usuario, como contenedores y modificadores, para Jetpack Compose.

    // Hilt (DI)
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler) // Procesador de anotaciones para Hilt, que genera el código necesario para la inyección de dependencias en tiempo de compilación.

    //Coil
    implementation(libs.coil.compose)

    //Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation ("io.mockk:mockk:1.13.10")


    // Project Modules
    implementation(project(":core:model"))
    implementation(project(":core:domain"))
    implementation(project(":core:navigation"))
    implementation(project(":utils"))
}
