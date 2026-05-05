plugins {
    id("java-library")
}

description = "tests-helper"
group = "skladinya.tests.helper"

dependencies {
    implementation(project(":domain"))
}