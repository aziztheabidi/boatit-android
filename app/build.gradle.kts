import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.google.services)
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktlint)
}

val boatitLocalProperties =
    Properties().apply {
        val f = rootProject.file("local.properties")
        if (f.exists()) {
            f.inputStream().use { load(it) }
        }
    }

fun boatitProp(
    name: String,
    default: String = "",
): String =
    (project.findProperty(name) as? String)?.takeIf { it.isNotBlank() }
        ?: boatitLocalProperties.getProperty(name)?.takeIf { it.isNotBlank() }
        ?: default

/**
 * When true, detekt and ktlint violations do not fail the build (local escape hatch only).
 * Set in `local.properties` or `-Pboatit.relaxStaticAnalysis=true`.
 *
 * Default is false so CI and `./gradlew build` enforce static analysis like a production pipeline.
 */
val boatitRelaxStaticAnalysis: Boolean =
    (project.findProperty("boatit.relaxStaticAnalysis") as? String)
        ?.equals("true", ignoreCase = true) == true ||
        boatitLocalProperties.getProperty("boatit.relaxStaticAnalysis")
            ?.equals("true", ignoreCase = true) == true

detekt {
    buildUponDefaultConfig = true
    allRules = false
    ignoreFailures = boatitRelaxStaticAnalysis
    parallel = true
}

ktlint {
    ignoreFailures.set(boatitRelaxStaticAnalysis)
    verbose.set(true)
}

android {
    namespace = "com.boatit.boatsharing"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.boatit.boat_it"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        resValue("string", "mapAPiKey", boatitProp("boatit.maps.apiKey"))
        resValue("string", "path", boatitProp("boatit.app.basePath"))
        resValue("string", "baseUrl", boatitProp("boatit.api.baseUrl"))

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            val baseUrl =
                boatitProp("boatit.debug.api.baseUrl").ifBlank {
                    boatitProp("boatit.api.baseUrl")
                }
            val appPath =
                boatitProp("boatit.debug.app.basePath").ifBlank {
                    boatitProp("boatit.app.basePath")
                }
            resValue("string", "baseUrl", baseUrl)
            resValue("string", "path", appPath)
        }
        release {
            resValue("string", "baseUrl", boatitProp("boatit.api.baseUrl"))
            resValue("string", "path", boatitProp("boatit.app.basePath"))
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
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
        compose = true
    }

    packaging {
        resources {
            excludes.add("META-INF/gradle/incremental.annotation.processors")
            excludes.add("META-INF/DEPENDENCIES")
            excludes.add("META-INF/LICENSE")
            excludes.add("META-INF/LICENSE.txt")
            excludes.add("META-INF/NOTICE")
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.places)
    implementation(libs.firebase.firestore.ktx)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.ktor.mock)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.googleMapsCompose)
    implementation(libs.play.services.location)

    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.livedata.ktx)

    implementation(libs.gson)

    implementation(libs.ktor.core)
    implementation(libs.ktor.cio)
    implementation(libs.ktor.negotiation)
    implementation(libs.ktor.json)
    implementation(libs.ktor.logging)
    implementation(libs.ktor.auth)

    implementation(libs.kotlinx.serialization)

    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.android.compose)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.messaging)
    implementation(libs.stripe.android)
    implementation(libs.compose.ratingbar)
    implementation(libs.accompanist.pager)
    implementation(libs.androidx.security.crypto)
}
