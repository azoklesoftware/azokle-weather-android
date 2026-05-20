

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.azokle.weather"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.azokle.weather"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        resourceConfigurations.addAll(
            listOf(
                "en",
                "es",
                "fr",
                "hr",
                "vi",
                "zh-rCN",
                "de",
                "ru",
                "pl",
                "sv",
                "nl",
                "it",
                "pt",
                "ja",
                "ko",
                "ar",
                "hi",
                "tr",
                "id",
                "th",
                "bn",
                "te",
                "mr",
                "ta",
                "ur",
                "gu",
                "kn",
                "ml",
                "or",
                "pa"
            )
        )
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
        release {
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        compose = true
    }

    kotlin {
        jvmToolchain(17)
        compilerOptions {
            freeCompilerArgs.addAll(
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:stabilityConfigurationPath=$rootDir/compose-stability-conf.txt"
            )
        }
    }
}

dependencies {
    // Core Android
    implementation("androidx.core:core-ktx:1.18.0")
    implementation("androidx.appcompat:appcompat:1.7.1")

    // Compose
    implementation(platform("androidx.compose:compose-bom:2026.05.00"))
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-compose:2.9.8")
    // Previews
    implementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-tooling-preview")

    // Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.11.0")
}