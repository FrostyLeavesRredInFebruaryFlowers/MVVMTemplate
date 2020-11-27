import app.web.drjackycv.buildsrc.Depends

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.library")
    kotlin("android")
    id("kotlin-parcelize")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
}

android {

    compileSdkVersion(Depends.Versions.androidCompileSdkVersion)

    defaultConfig {
        multiDexEnabled = true
        vectorDrawables.useSupportLibrary = true
        minSdkVersion(Depends.Versions.minSdkVersion)
        targetSdkVersion(Depends.Versions.targetSdkVersion)
        versionCode = Depends.Versions.appVersionCode
        versionName = Depends.generateVersionName()
        testInstrumentationRunner =
            Depends.Versions.testInstrumentationRunner
        consumerProguardFiles("consumer-rules.pro")
    }
    compileOptions {
        targetCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    viewBinding {
        isEnabled = true
    }

    sourceSets {
        map { it.java.srcDir("src/${it.name}/kotlin") }
    }
    buildTypes {
        named("debug") { }
        named("release") {
            isMinifyEnabled = true
            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            )
        }
    }

    packagingOptions {
        exclude("META-INF/rxjava.properties")
        exclude("META-INF/proguard/androidx-annotations.pro")
        exclude("error_prone/Annotations.gwt.xml")
        exclude("third_party/java_src/error_prone/project/annotations/Annotations.gwt.xml")
        exclude("third_party/java_src/error_prone/project/annotations/Google_internal.gwt.xml")
        exclude("jsr305_annotations/Jsr305_annotations.gwt.xml")
        exclude("META-INF/AL2.0")
        exclude("META-INF/LGPL2.1")
    }

}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        freeCompilerArgs = listOf(
            "-XXLanguage:+InlineClasses",
            "-Xallow-jvm-ir-dependencies",
            "-Xskip-prerelease-check"
        )
    }
}

dependencies {
    implementation(Depends.Libraries.kotlin)
    implementation(Depends.Libraries.kotlin_reflect)

    //dependency injection
    implementation(Depends.Libraries.hilt_android)
    kapt(Depends.Libraries.hilt_android_compiler)
    implementation(Depends.Libraries.hilt_lifecycle_viewmodel)
    kapt(Depends.Libraries.hilt_compiler)
    implementation(Depends.Libraries.java_inject)
    //other
    implementation(Depends.Libraries.timber)
    //android
    implementation(Depends.Libraries.appcompat)
    implementation(Depends.Libraries.constraintlayout)
    implementation(Depends.Libraries.material)
    implementation(Depends.Libraries.navigation_fragment_ktx)
    implementation(Depends.Libraries.navigation_ui_ktx)
    implementation(Depends.Libraries.paging_runtime_ktx)
    implementation(Depends.Libraries.paging_rx_ktx)
    implementation(Depends.Libraries.android_lifecycle_extensions)
    implementation(Depends.Libraries.lifecycle_livedata_ktx)
    implementation(Depends.Libraries.lifecycle_runtime_ktx)
    implementation(Depends.Libraries.lifecycle_common_java8)
    implementation(Depends.Libraries.lifecycle_viewmodel_ktx)
    implementation(Depends.Libraries.multidex)
    implementation(Depends.Libraries.android_core_ktx)
    implementation(Depends.Libraries.fragment_ktx)
    implementation(Depends.Libraries.recyclerview)
    //compose
    implementation(Depends.Libraries.compose_foundation)
    implementation(Depends.Libraries.compose_foundation_layout)
    implementation(Depends.Libraries.compose_material)
    implementation(Depends.Libraries.compose_runtime_saved_instance_state)
    implementation(Depends.Libraries.compose_navigation)
    implementation(Depends.Libraries.ui_test)
    implementation(Depends.Libraries.ui_tooling)
    implementation(Depends.Libraries.ui_framework)
    implementation(Depends.Libraries.ui_foundation)
    implementation(Depends.Libraries.ui_layout)
    implementation(Depends.Libraries.ui_material)
    //reactive
    implementation(Depends.Libraries.rx_java_android)
    implementation(Depends.Libraries.rx_binding3)
    implementation(Depends.Libraries.rx_kotlin)
    implementation(Depends.Libraries.autodispose)
    implementation(Depends.Libraries.autodispose_android)
    implementation(Depends.Libraries.autodispose_android_arch)
    //ui
    implementation(Depends.Libraries.glide)
    kapt(Depends.Libraries.glide_compiler)
    implementation(Depends.Libraries.lottie)
    //android architecture component
    implementation(Depends.Libraries.android_lifecycle_extensions)
    //test
    androidTestImplementation(Depends.Libraries.test_runner)
    androidTestImplementation(Depends.Libraries.test_rules)
    androidTestImplementation(Depends.Libraries.test_core)
    androidTestImplementation(Depends.Libraries.test_ext_junit)
    androidTestImplementation(Depends.Libraries.espresso_core)

    implementation(project(":domain"))
}
