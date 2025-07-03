plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.android.hilt)
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "com.mleon.feature.orders"
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

    //Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation("org.mockito:mockito-core:5.11.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation ("io.mockk:mockk:1.13.10")

    //Material Design
    implementation(libs.androidx.material3.android)
    implementation(libs.material.icons.extended)

    //Compose
    implementation(libs.androidx.ui) //dependencia principal de compose
    implementation(libs.androidx.ui.tooling.preview) // vista previa para compose
    implementation(libs.androidx.foundation.android) // Proporciona componentes básicos de la interfaz de usuario, como contenedores y modificadores, para Jetpack Compose.

    // Hilt (DI)
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler) // Procesador de anotaciones para Hilt, que genera el código necesario para la inyección de dependencias en tiempo de compilación.
    implementation(libs.androidx.hilt.navigation.compose) // Integración de Hilt con Jetpack Compose para la inyección de dependencias en composables.

    // Project Modules
    implementation(project(":core:navigation"))
    implementation(project(":core:model"))
    implementation(project(":utils"))
    implementation(project(":core:data"))
}