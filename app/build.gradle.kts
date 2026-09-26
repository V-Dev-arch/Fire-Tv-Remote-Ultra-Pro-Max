import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    // google-services is provided by the *private buildable* codebase repo;
    // this showcase repo omits Firebase wiring intentionally.
}

android {
    namespace = "com.ultraprodev.firetvremote"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ultraprodev.firetvremote"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    // Compose UI
    implementation(platform("androidx.compose:compose-bom:latest.release"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.activity:activity-compose")
    implementation("androidx.navigation:navigation-compose")

    // Material icons
    implementation("androidx.compose.material:material-icons-extended")

    // Core AndroidX
    implementation("androidx.core:core-ktx")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android")

    // Image loading for app tile logos
    implementation("io.coil-kt:coil-compose")
}

// The actual app also pulls in:
//   · Firebase Authentication + Firebase BOM
//   · Supabase Kotlin SDK (createSupabaseClient + Postgrest)
//   · Firebase Third-Party Auth integration on Supabase
// Those dependencies are intentionally absent from this showcase repo —
// see the private Fire-Tv-Remote-Ultra-Pro-Max-Codebase repo.
