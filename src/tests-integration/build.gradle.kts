plugins {
    id("java-library")
}

description = "tests-integration"
group = "skladinya.tests.integration"

dependencies {
    implementation(project(":domain"))
    implementation(project(":tests-helper"))
    // Add project dependencies here

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // Add other dependencies here
}

tasks.test {
    useJUnitPlatform()
}

// Add integration tests to the src/test/java in that module to package skladinya.tests.integration