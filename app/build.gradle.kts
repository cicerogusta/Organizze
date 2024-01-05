plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")

}

android {
    namespace = "com.cicerodev.yourmoney"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.cicerodev.yourmoney"
        minSdk = 26
        targetSdk = 33
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

        buildFeatures {
            viewBinding = true
            dataBinding = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    //ktx android
    implementation ("androidx.core:core-ktx:1.10.1")
    implementation ("androidx.fragment:fragment-ktx:1.6.1")
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation ("androidx.activity:activity-ktx:1.7.2")
    implementation ("com.google.android.gms:play-services-ads:22.3.0")
    implementation ("com.github.timonknispel:KTLoadingButton:1.2.0")

//    hilt
    implementation("com.google.dagger:hilt-android:2.48")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    kapt("com.google.dagger:hilt-compiler:2.48")


    implementation("androidx.core:core-ktx:1.9.0")
    implementation("com.github.clans:fab:1.6.4")
    implementation("com.github.prolificinteractive:material-calendarview:2.0.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-auth-ktx:22.1.1")
    implementation("com.google.firebase:firebase-database-ktx:20.2.2")
    implementation("com.heinrichreimersoftware:material-intro:2.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

//    Mockito
    testImplementation("org.mockito:mockito-core:3.12.4")
    androidTestImplementation("org.mockito:mockito-android:2.28.2")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.arch.core:core-testing:2.2.0")
}
kapt {
    correctErrorTypes = true
}