plugins {
    id("java-library")
}

description = "tests-unit"
group = "skladinya.tests.unit"

dependencies {
    implementation(project(":domain"))
    implementation(project(":tests-helper"))
    implementation(project(":services-auth"))
    implementation(project(":services-user"))
    // Add project dependencies here

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.mockito:mockito-core:5.23.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.23.0")
    // Add other dependencies here
}

tasks.test {
    useJUnitPlatform()
}

// Add unit tests to the src/test/java in that module to package skladinya.tests.unit.<rest.path.to.tested.class.package>