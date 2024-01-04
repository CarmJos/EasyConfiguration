```text
   ____                _____          ____                    __  _
  / __/__ ____ __ __  / ___/__  ___  / _(_)__ ___ _________ _/ /_(_)__  ___
 / _// _ `(_-</ // / / /__/ _ \/ _ \/ _/ / _ `/ // / __/ _ `/ __/ / _ \/ _ \
/___/\_,_/___/\_, /  \___/\___/_//_/_//_/\_, /\_,_/_/  \_,_/\__/_/\___/_//_/
             /___/                      /___/
```

README LANGUAGES [ [**English**](README.md) | [中文](README_CN.md)  ]

# EasyConfiguration

[![version](https://img.shields.io/github/v/release/CarmJos/EasyConfiguration)](https://github.com/CarmJos/EasyConfiguration/releases)
[![License](https://img.shields.io/github/license/CarmJos/EasyConfiguration)](https://www.gnu.org/licenses/lgpl-3.0.html)
[![workflow](https://github.com/CarmJos/EasyConfiguration/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/CarmJos/EasyConfiguration/actions/workflows/maven.yml)
[![CodeFactor](https://www.codefactor.io/repository/github/carmjos/easyconfiguration/badge)](https://www.codefactor.io/repository/github/carmjos/easyconfiguration)
![CodeSize](https://img.shields.io/github/languages/code-size/CarmJos/EasyConfiguration)
![](https://visitor-badge.glitch.me/badge?page_id=EasyConfiguration.readme)

**Easy _(to make)_ Configurations!**

A simple, easy-to-use and universal solution for managing configuration files.
Enjoy the ease of use with customizable formats for loading, reading, and updating your configuration files.

## Features & Advantages

Supported [YAML](impl/yaml), [JSON](impl/json), [HOCON](impl/hocon) and [SQL](impl/sql) based configuration files
format.

- Class-based mechanism for initializing, loading, retrieving, and updating configuration files, ensuring convenience
  and efficiency.
- Supports manual serialization and deserialization of complex configurations.
- Offers multiple builder forms for rapid construction of `ConfigValue<?>` objects.
- Enables specification of configuration paths, comments, and more via annotations.

## Development

For the latest JavaDoc release, [CLICK HERE](https://CarmJos.github.io/EasyConfiguration).

For a detailed development guide, [CLICK HERE](.doc/README.md).

### Code Samples

To quickly demonstrate the applicability of the project, here are a few practical demonstrations:
- [Database configuration.](demo/src/main/java/cc/carm/lib/configuration/demo/DatabaseConfiguration.java)
- [Demonstration of all types of configuration instance classes.](demo/src/main/java/cc/carm/lib/configuration/demo/tests/conf/DemoConfiguration.java)

Check out all code demonstrations [HERE](demo/src/main/java/cc/carm/lib/configuration/demo/DatabaseConfiguration.java).
For more examples, see the [Development Guide](.doc/README.md).

```java
public class Sample {

  @HeaderComment("Configurations for sample")
  interface SampleConfig extends Configuration {

    @InlineComment("Enabled?") // Inline comment
    ConfiguredValue<Boolean> ENABLED = ConfiguredValue.of(true);

    ConfiguredList<UUID> UUIDS = ConfiguredList.builderOf(UUID.class).fromString()
            .parseValue(UUID::fromString).serializeValue(UUID::toString)
            .defaults(
                    UUID.fromString("00000000-0000-0000-0000-000000000000"),
                    UUID.fromString("00000000-0000-0000-0000-000000000001")
            ).build();
    
    interface INFO extends Configuration {
        
      @HeaderComment("Configure your name!") // Header comment
      ConfiguredValue<String> NAME = ConfiguredValue.of("Joker");

      @ConfigPath("year") // Custom path
      ConfiguredValue<Integer> AGE = ConfiguredValue.of(24);
      
    }
  }

  public static void main(String[] args) {
    // 1. Make a configuration provider from a file.
    ConfigurationProvider<?> provider = EasyConfiguration.from("config.yml");
    // 2. Initialize the configuration classes or instances.
    provider.initialize(SampleConfig.class);
    // 3. Enjoy using the configuration!
    SampleConfig.ENABLED.set(false);
    System.out.println("Your name is " + SampleConfig.INFO.NAME.getNotNull() + " !");
  }

}

```

```yaml
# Configurations for sample

enabled: true # Enabled?

uuids:
  - 00000000-0000-0000-0000-000000000000
  - 00000000-0000-0000-0000-000000000001

info:
  # Configure your name!
  name: Joker
  year: 24
```

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

This project's source code is licensed under
the [GNU LESSER GENERAL PUBLIC LICENSE](https://www.gnu.org/licenses/lgpl-3.0.html).
