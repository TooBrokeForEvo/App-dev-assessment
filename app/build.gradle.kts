plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.diaryapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.diaryapp"
        minSdk = 24
        targetSdk = 36
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.viewpager2)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.lifecycle.runtime.ktx)


        val room_version = "2.8.4"
        implementation("androidx.room:room-runtime:$room_version")
        ksp("androidx.room:room-compiler:$room_version")
        annotationProcessor("androidx.room:room-compiler:$room_version")
        implementation("androidx.room:room-ktx:$room_version")
        implementation("androidx.room:room-rxjava2:$room_version")
        implementation("androidx.room:room-rxjava3:$room_version")
        implementation("androidx.room:room-guava:$room_version")
        testImplementation("androidx.room:room-testing:$room_version")
        implementation("androidx.room:room-paging:$room_version")
        implementation("androidx.recyclerview:recyclerview:1.4.0")
        implementation("androidx.recyclerview:recyclerview-selection:1.2.0")
        implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
        implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
        implementation("com.github.bumptech.glide:glide:4.16.0")


}
