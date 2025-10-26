plugins {
    id("java")
    id("application")

}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("com.github.javafaker:javafaker:1.0.2")
    implementation("com.google.code.gson:gson:2.13.2")
    implementation("org.json:json:20250517")
}

tasks.test {
    useJUnitPlatform()
}

application {

    mainClass = "org.example.Main"
}
