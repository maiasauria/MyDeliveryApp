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
            buildConfigField("String", "CLOUDINARY_API_KEY", "\"${project.findProperty("CLOUDINARY_API_KEY") ?: ""}\"")
            buildConfigField("String", "CLOUDINARY_API_SECRET", "\"${project.findProperty("CLOUDINARY_API_SECRET") ?: ""}\"")
            buildConfigField("String", "CLOUDINARY_CLOUD_NAME", "\"${project.findProperty("CLOUDINARY_CLOUD_NAME") ?: ""}\"")
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
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Cloudinary
    implementation("com.cloudinary:cloudinary-android:2.3.1") // Biblioteca para interactuar con Cloudinary, un servicio de gestión de medios en la nube.

    // Project Modules
    implementation(project(":core:model"))
}
