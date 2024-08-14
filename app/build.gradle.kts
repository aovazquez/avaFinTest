import java.util.Locale

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.devtools.ksp")
    id ("dagger.hilt.android.plugin")
    id ("kotlin-parcelize")
    id ("org.sonarqube") version "3.5.0.2730"
    id ("jacoco")
}


android {
    namespace = "com.mx.avafintest"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mx.avafintest"
        minSdk = 24
        targetSdk = 34
        versionCode = 2
        versionName = "1.0.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
        debug {
            enableAndroidTestCoverage = true
            enableUnitTestCoverage = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures.viewBinding = true
}

jacoco {
    toolVersion = "0.8.11"
}

val exclusions = listOf(
    "**/R.class",
    "**/R$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*",
    "**/*Test*.*",
    "**/com/mx/avafintest/databinding/*",
    "**/com/mx/avafintest/generated/callback/*",
    "**/android/databinding/*",
    "**/androidx/databinding/*",
    "**/di/module/*",
    "**/*MapperImpl*.*",
    "**/BuildConfig.*",
    "**/*Component*.*",
    "**/*BR*.*",
    "**/Manifest*.*",
    "**/*Companion*.*",
    "**/*Module.*", /* filtering Dagger modules classes */
    "**/*Dagger*.*",/* filtering Dagger-generated classes */
    "**/*MembersInjector*.*",
    "**/*_Factory*.*",
    "**/*_Provide*Factory*.*",
    "**/*Extensions*.*",
    "**/*$Result.*", /* filtering `sealed` and `data` classes */
    "**/*$Result$*.*",/* filtering `sealed` and `data` classes */
    "**/*Args*.*", /* filtering Navigation Component generated classes */
    "**/*Directions*.*" /* filtering Navigation Component generated classes */
)

tasks.withType(Test::class) {
    configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}

android {
    applicationVariants.all(closureOf<com.android.build.gradle.internal.api.BaseVariantImpl> {
        val variant = this@closureOf.name.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }

        val unitTests = "test${variant}UnitTest"
        val androidTests = "connected${variant}AndroidTest"

        tasks.register<JacocoReport>("Jacoco${variant}CodeCoverage") {
            dependsOn(listOf(unitTests, androidTests))
            group = "Reporting"
            description = "Execute ui and unit tests, generate and combine Jacoco coverage report"
            reports {
                xml.required.set(true)
                html.required.set(true)
            }
            sourceDirectories.setFrom(layout.projectDirectory.dir("src/main"))
            classDirectories.setFrom(files(
                fileTree(layout.buildDirectory.dir("intermediates/javac/")) {
                    exclude(exclusions)
                },
                fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/")) {
                    exclude(exclusions)
                }
            ))
            executionData.setFrom(files(
                fileTree(layout.buildDirectory) { include(listOf("**/*.exec", "**/*.ec")) }
            ))
        }
    })
}

sonarqube {
    properties {
        property("sonar.projectName", "TestAvafin")
        property ("sonar.projectKey", "TestAvafin")
        property ("sonar.projectVersion", "1.0.0")
        property ("sonar.host.url", "http://localhost:9000")
        property ("sonar.language", "kotlin")
        property ("sonar.sources", "src/main/java")
        property ("sonar.sourceEncoding", "UTF-8")
        property ("sonar.tests", "src/test/java")
        property ("sonar.java.coveragePlugin", "jacoco")
        property ("sonar.coverage.jacoco.xmlReportPaths", "$buildDir/reports/jacoco/JacocoDebugCodeCoverage/JacocoDebugCodeCoverage.xml")
        property ("sonar.java.reportPath", "**/jacoco/*.exec")
        property ("sonar.java.binaries=target/classes", "app/build/tmp/kotlin-classes")
        // property ("sonar.test.inclusions", "**/*Test*/**")
        // property ("sonar.sourceEncoding", "UTF-8")
        // property ("sonar.sources", "src/main/java")
        property ("sonar.exclusions", "**/*Test*/**," +
                "*.json," +
                "**/*test*/**," +
                "**/.gradle/**," +
                "**/R.class," +
                "**/R$*.class")
        property("sonar.coverage.exclusions", "**/src/test/**, **/*Test*.*")

        // property ("sonar.coverage.jacoco.xmlReportPaths", "$buildDir/reports/jacoco/jacocoTestReport/jacocoTestReport.xml")

    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)

    implementation (libs.androidx.lifecycle.runtime.ktx)

    implementation (libs.kotlinx.coroutines.android)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    ksp(libs.androidx.hilt.compiler)

    // Retrofit
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.converter.scalars)
    implementation (libs.adapter.rxjava2)
    implementation (libs.logging.interceptor)

    testImplementation(libs.junit)
    testImplementation (libs.mockk)
    testImplementation (libs.kotlinx.coroutines.test)
    testImplementation (libs.androidx.core.testing)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}