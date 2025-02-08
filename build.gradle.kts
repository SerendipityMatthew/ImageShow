import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

group = "com.imageshow"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
    maven ("https://jitpack.io" )

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
