plugins {
    id("java")
}

group = "com.wolfifurr"
version = "1.0.3"

repositories {
    mavenCentral()
}
val appData = System.getenv("APPDATA")
dependencies {
    compileOnly( files("$appData/Hytale/install/release/package/game/latest/Server/HytaleServer.jar"))
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}