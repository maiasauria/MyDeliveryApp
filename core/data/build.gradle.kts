import java.io.FileInputStream
import java.util.Properties

val secretsProperties = Properties().apply {
    val secretsFile = rootProject.file("secrets.properties")
    if (secretsFile.exists()) {
        load(FileInputStream(secretsFile))
    }
}

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("com.google.dagger.hilt.android") version "2.56.2"
    id("com.google.devtools.ksp") version "2.1.21-2.0.1"
}

android {
    namespace = "com.mleon.core.data"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        defaultConfig {
            buildConfigField(
                "String",
                "CLOUDINARY_API_KEY",
                "\"${secretsProperties["CLOUDINARY_API_KEY"] ?: ""}\""
            )
            buildConfigField(
                "String",
                "CLOUDINARY_API_SECRET",
                "\"${secretsProperties["CLOUDINARY_API_SECRET"] ?: ""}\""
            )
            buildConfigField(
                "String",
                "CLOUDINARY_CLOUD_NAME",
                "\"${secretsProperties["CLOUDINARY_CLOUD_NAME"] ?: ""}\""
            )
            buildConfigField("String", "API_URL", "\"${secretsProperties["API_URL"] ?: ""}\"")
            buildConfigField(
                "String",
                "API_ENCRYPTION_KEY",
                "\"${secretsProperties["API_ENCRYPTION_KEY"] ?: ""}\""
            )
        }
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
    buildFeatures {
        buildConfig = true
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
    implementation(libs.material) // Material Design Components

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    // Hilt (DI)
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.common)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
// Mockito for mocking
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)
// Kotlin coroutines test
    testImplementation(libs.kotlinx.coroutines.test)
// Turbine for Flow testing
    testImplementation(libs.turbine)

    // Room (runtime, compiler, ktx)
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.work.runtime.ktx)

    //Cloudinary
    implementation(libs.cloudinary.android) // Biblioteca para interactuar con Cloudinary, un servicio de gestión de medios en la nube.

    // Project Modules
    implementation(project(":core:model"))
    implementation(project(":utils"))


}
