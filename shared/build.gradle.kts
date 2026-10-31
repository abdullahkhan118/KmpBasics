import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.net.URI

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room3)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    android {
        namespace = "io.github.abdullahkhan118.kmpbasics.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
        androidResources {
            enable = true
        }
        withHostTest {
            isIncludeAndroidResources = true
        }
        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.compose.uiTooling)
            implementation(libs.ktor.client.android)

            // Optional when using Room SQLite Wrapper
            implementation(libs.androidx.room3.sqlite.wrapper)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonMain.dependencies {
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.composeViewmodel)
            implementation(libs.koin.annotations)
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.jetbrains.navigation3.ui)
            implementation(libs.jetbrains.lifecycle.viewmodelNavigation3)
            implementation(libs.jetbrains.material3.adaptiveNavigation3)
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
            implementation(libs.ktor.client.core)

            // Contains a tiny subset of standard icons (Home, Share, ArrowBack, etc.)
            implementation(libs.compose.materialIconsCore)

            // Contains the massive 10,000+ icon pack (Not recommended due to build lag)
            implementation(compose.materialIconsExtended)

            implementation(libs.paging.common)
            implementation(libs.paging.compose)

            implementation(libs.androidx.room3.runtime)
            implementation(libs.androidx.sqlite.bundled)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

room3 {
    schemaDirectory("$projectDir/schemas")
}

ksp {
    arg("room.generateKotlin", "true")
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)

    add("kspAndroid", libs.koin.kspCompiler)
    add("kspIosArm64", libs.koin.kspCompiler)
    add("kspIosSimulatorArm64", libs.koin.kspCompiler)
    add("kspCommonMainMetadata", libs.koin.kspCompiler)

    add("kspAndroid", libs.androidx.room3.compiler)
    add("kspIosArm64", libs.androidx.room3.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room3.compiler)
}

// Let commonMain see the code KSP generates from @Module/@Single/etc.
kotlin.sourceSets.commonMain {
    kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
}

tasks.matching { it.name != "kspCommonMainKotlinMetadata" && it.name.startsWith("ksp") }.configureEach {
    dependsOn("kspCommonMainKotlinMetadata")
}

tasks.named("prepareComposeResourcesTaskForCommonMain") {
    dependsOn("refreshConfigDefaults")
}

tasks.register("refreshConfigDefaults") {
    val configDefaultsFile = layout.projectDirectory.file("src/commonMain/composeResources/files/config_defaults.json").asFile

    doLast {
        val response = URI("https://tomoe.bykea.net/api/v1/common/services").toURL().readText()
        configDefaultsFile.parentFile?.mkdirs()
        if (!configDefaultsFile.exists()) configDefaultsFile.createNewFile()
        configDefaultsFile.writeText(response)
        println("Config defaults: Updated")
    }
}