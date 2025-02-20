import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    kotlin("plugin.serialization") version "2.1.0"
    id("com.google.devtools.ksp") version "2.1.10-1.0.30" // Depends on your kotlin version

}

group = "com.imageshow"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
    maven("https://jitpack.io")
    maven("https://jogamp.org/deployment/maven")

}
kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }

}


dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)
    implementation("io.coil-kt.coil3:coil-compose:3.1.0")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.1.0")
    implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:2.8.2")

    implementation("io.insert-koin:koin-core:4.0.2")
    implementation("io.insert-koin:koin-core-viewmodel:4.0.2")
    implementation("io.insert-koin:koin-core-coroutines:4.0.2")
    implementation("io.insert-koin:koin-compose-viewmodel:4.0.2")
    implementation("com.ashampoo:kim:0.22.2")
    implementation("ovh.plrapps:mapcompose-mp:0.9.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.8.0")
    implementation("dev.chrisbanes.haze:haze:1.3.1")
    implementation("dev.datlag:kcef:2024.04.20.4")
    api("io.github.kevinnzou:compose-webview-multiplatform:1.8.4")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.8.0")
    implementation("org.jetbrains.compose.web:web-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    implementation("com.arkivanov.decompose:decompose:3.2.2")
    implementation("com.arkivanov.decompose:extensions-compose:3.2.2")
    implementation("com.arkivanov.essenty:lifecycle-jvm:2.4.0")


}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "ImageShow"
            packageVersion = "1.0.0"
        }
    }
}


afterEvaluate {
    tasks.withType<JavaExec> {
        jvmArgs("--add-opens", "java.desktop/sun.awt=ALL-UNNAMED")
        jvmArgs("--add-opens", "java.desktop/java.awt.peer=ALL-UNNAMED")

        if (System.getProperty("os.name").contains("Mac")) {
            jvmArgs("--add-opens", "java.desktop/sun.awt=ALL-UNNAMED")
            jvmArgs("--add-opens", "java.desktop/sun.lwawt=ALL-UNNAMED")
            jvmArgs("--add-opens", "java.desktop/sun.lwawt.macosx=ALL-UNNAMED")
        }
    }
}
