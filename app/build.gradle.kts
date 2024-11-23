    plugins {
        alias(libs.plugins.android.application)
        alias(libs.plugins.google.gms.google.services)
    }

    android {
        namespace = "com.sak.musicplayer"
        compileSdk = 34

        defaultConfig {
            applicationId = "com.sak.musicplayer"
            minSdk = 30
            targetSdk = 34
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
                viewBinding =true
            }
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
        buildFeatures {
            viewBinding = true
        }
    }

    dependencies {

        implementation(libs.appcompat)
        implementation(libs.material)
        implementation(libs.activity)
        implementation(libs.constraintlayout)
        implementation(libs.firebase.firestore)
        implementation(libs.firebase.database)
        implementation(libs.media3.common)
        implementation(libs.navigation.fragment)
        implementation(libs.navigation.ui)
        implementation(libs.firebase.auth)
        implementation(libs.firebase.storage)
        testImplementation(libs.junit)
        androidTestImplementation(libs.ext.junit)
        androidTestImplementation(libs.espresso.core)
        implementation ("com.squareup.picasso:picasso:2.8")
            implementation ("com.squareup.retrofit2:retrofit:2.8.1")
            implementation ("com.squareup.retrofit2:converter-gson:2.8.1")
            implementation ("com.squareup.okhttp3:logging-interceptor:4.9.1")
        implementation ("pl.droidsonroids.gif:android-gif-drawable:1.2.17")
        implementation("com.google.android.exoplayer:exoplayer:2.19.1")
            implementation ("com.google.code.gson:gson:2.10.1")
        implementation ("com.github.ibrahimsn98:SmoothBottomBar:1.7.9")
        implementation ("com.github.bumptech.glide:glide:4.12.0")
        annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
        implementation ("de.hdodenhof:circleimageview:3.1.0")
        implementation ("dev.shreyaspatil.EasyUpiPayment:EasyUpiPayment:3.0.2")



        implementation ("com.spotify.android:auth:1.2.3")

    }