以下是上述中文文档的英文翻译，尽可能保留原意的同时，调整为更适合美国读者的表述方式：

---

# EasyConfiguration

[![version](https://img.shields.io/github/v/release/CarmJos/EasyConfiguration)](https://github.com/CarmJos/EasyConfiguration/releases)
[![License](https://img.shields.io/github/license/CarmJos/EasyConfiguration)](https://www.gnu.org/licenses/lgpl-3.0.html)
[![workflow](https://github.com/CarmJos/EasyConfiguration/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/CarmJos/EasyConfiguration/actions/workflows/maven.yml)
[![CodeFactor](https://www.codefactor.io/repository/github/carmjos/easyconfiguration/badge)](https://www.codefactor.io/repository/github/carmjos/easyconfiguration)
![CodeSize](https://img.shields.io/github/languages/code-size/CarmJos/EasyConfiguration)
![](https://visitor-badge.glitch.me/badge?page_id=EasyConfiguration.readme)

**Easy _(to make)_ Configurations!**

Introducing EasyConfiguration, your simple and universal solution for managing configuration files. Enjoy the ease of use with customizable formats for loading, reading, and updating your configuration files.

## Advantages

- Class-based mechanism for initializing, loading, retrieving, and updating configuration files, ensuring convenience and efficiency.
- Supports manual serialization and deserialization of complex configurations.
- Offers multiple builder forms for rapid construction of `ConfigValue<?>` objects.
- Enables specification of configuration paths, comments, and more via annotations.

## Development

For a detailed development guide, [click here](.doc/README.md). For the latest JavaDoc release, [click here](https://CarmJos.github.io/EasyConfiguration).

### Code Samples

Check out some code demonstrations [here](demo/src/main/java/cc/carm/lib/configuration/demo/DatabaseConfiguration.java). For more examples, see the [development guide](.doc/README.md).

### Dependencies

#### Maven Dependency

<details>
<summary>Remote Repository Configuration</summary>

```xml
<project>
    <repositories>

        <repository>
            <!-- Using Maven Central Repository for secure and stable updates, though synchronization might be needed. -->
            <id>maven</id>
            <name>Maven Central</name>
            <url>https://repo1.maven.org/maven2</url>
        </repository>
  
        <repository>
            <!-- Using GitHub dependencies for real-time updates, configuration required (recommended). -->
            <id>EasyConfiguration</id>
            <name>GitHub Packages</name>
            <url>https://maven.pkg.github.com/CarmJos/EasyConfiguration</url>
        </repository>

    </repositories>
</project>
```

</details>

<details>
<summary>Generic Native Dependency</summary>

```xml
<project>
    <dependencies>
        <!-- Basic implementation part, requiring custom implementation of “Provider” and “Wrapper”. -->
        <dependency>
            <groupId>cc.carm.lib</groupId>
            <artifactId>easyconfiguration-core</artifactId>
            <version>[LATEST RELEASE]</version>
            <scope>compile</scope>
        </dependency>

        <!-- YAML file-based implementation, compatible with all Java environments. -->
        <dependency>
            <groupId>cc.carm.lib</groupId>
            <artifactId>easyconfiguration-yaml</artifactId>
            <version>[LATEST RELEASE]</version>
            <scope>compile</scope>
        </dependency>

        <!-- JSON file-based implementation, compatible with all Java environments. Note: JSON does not support file comments. -->
        <dependency>
            <groupId>cc.carm.lib</groupId>
            <artifactId>easyconfiguration-json</artifactId>
            <version>[LATEST RELEASE]</version>
            <scope>compile</scope>
        </dependency>

    </dependencies>
</project>
```

</details>

#### Gradle Dependency

<details>
<summary>Remote Repository Configuration</summary>

```groovy
repositories {

    // Using Maven Central Repository for secure and stable updates, though synchronization might be needed.
    mavenCentral()
  
    // Using GitHub dependencies for real-time updates, configuration required (recommended).
    maven { url 'https://maven.pkg.github.com/CarmJos/EasyConfiguration' }
    
}
```

</details>

<details>
<summary>Generic Native Dependency</summary>

```groovy

dependencies {

    // Basic implementation part, requiring custom implementation of “Provider” and “Wrapper”.
    api "cc.carm.lib:easyconfiguration-core:[LATEST RELEASE]"

    // YAML file-based implementation, compatible with all Java environments.
    api "cc.carm.lib:easyconfiguration-yaml:[LATEST RELEASE]"

    // JSON file-based implementation, compatible with all Java environments. Note: JSON does not support file comments.
    api "cc.carm.lib:easyconfiguration-json:[LATEST RELEASE]"

}
```

</details>

## Derived Projects

### [**MineConfiguration**](https://github.com/CarmJos/MineConfiguration) (by @CarmJos)

EasyConfiguration for MineCraft!
Easily manage configurations on MineCraft-related server platforms.

Currently supports BungeeCord, Bukkit (Spigot) servers, with more platforms to be supported soon.

## Support and Donation

If you appreciate this plugin, consider supporting me with a donation!

Thank you for supporting open-source projects!

Many thanks to Jetbrains for kindly providing a license for us to work on this and other open-source projects.  
[![](https://resources.jetbrains.com/storage/products/company/brand/logos/jb_beam.svg)](https://www.jetbrains.com/?from=https://github.com/ArtformGames/ResidenceList)


## Open Source License

This project's source code is licensed under the [GNU LESSER GENERAL PUBLIC LICENSE](https://www.gnu.org/licenses/lgpl-3.0.html).
