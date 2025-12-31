plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.e_rental"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.e_rental"
        minSdk = 24
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
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // 1. Firebase BoM (Cukup satu baris ini untuk mengatur versi semua library Firebase)
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))

    // 2. Firebase Libraries (Tanpa menuliskan versi karena sudah diatur oleh BoM)
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")

    // 3. Google Sign-In & Browser (Gunakan kutip dua tanpa titik koma di KTS)
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("androidx.browser:browser:1.7.0")

    // 4. UI Components
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // 5. Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}