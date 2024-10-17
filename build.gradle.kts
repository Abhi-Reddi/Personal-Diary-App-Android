// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google() // Ensure this is included
        mavenCentral()
    }
    dependencies {
        classpath("com.google.gms:google-services:4.3.15") // Updated to the latest version
    }
}

plugins {
    id("com.android.application") version "8.2.0" apply false
    id("com.android.library") version "8.2.0" apply false
}

// Add the google-services plugin at the bottom if using Gradle 8.x and above
