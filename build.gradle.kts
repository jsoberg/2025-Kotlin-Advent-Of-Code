plugins {
    kotlin("jvm") version libs.versions.kotlin
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

tasks {
    wrapper {
        gradleVersion = "9.2.1"
    }
}


dependencies {
    implementation(libs.kotlin.coroutines)
    implementation(libs.soberg.aoc.utilities)
}