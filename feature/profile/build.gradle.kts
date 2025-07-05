plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.android.hilt)
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

    // Kotlin Stdlib & Core KTX
    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    //Compose
    implementation(platform(libs.androidx.compose.bom)) // se usa para manejar versiones de Compose
    implementation(libs.androidx.ui) //dependencia principal de compose
    implementation(libs.androidx.ui.tooling.preview) // vista previa para compose
    implementation(libs.androidx.foundation.android) // Proporciona componentes básicos de la interfaz de usuario, como contenedores y modificadores, para Jetpack Compose.
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.activity.compose) // Proporciona integración de Jetpack Compose con actividades de Android.

    //Coil
    implementation(libs.coil.compose)

    // Hilt (DI)
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler) // Procesador de anotaciones para Hilt, que genera el código necesario para la inyección de dependencias en tiempo de compilación.
    implementation(libs.androidx.hilt.navigation.compose) // Integración de Hilt con Jetpack Compose para la inyección de dependencias en composables.

    //Cloudinary
    implementation("com.cloudinary:cloudinary-android:2.3.1") // Biblioteca para interactuar con Cloudinary, un servicio de gestión de medios en la nube.

    //Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation ("io.mockk:mockk:1.13.10")

    // Project Modules
    implementation(project(":utils"))
    implementation(project(":core:model"))
    implementation(project(":core:data"))
    implementation(project(":core:navigation"))
}