# EasyConfiguration-YAML

YAML file-based implementation, compatible with all Java environments.

## Dependencies

### Maven Dependency

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

```xml

<project>
    <dependencies>
        <dependency>
            <groupId>cc.carm.lib</groupId>
            <artifactId>easyconfiguration-yaml</artifactId>
            <version>[LATEST RELEASE]</version>
            <scope>compile</scope>
        </dependency>
    </dependencies>
</project>
```

### Gradle Dependency

```groovy
repositories {

    // Using Maven Central Repository for secure and stable updates, though synchronization might be needed.
    mavenCentral()

    // Using GitHub dependencies for real-time updates, configuration required (recommended).
    maven { url 'https://maven.pkg.github.com/CarmJos/EasyConfiguration' }

}
```

```groovy
dependencies {
    api "cc.carm.lib:easyconfiguration-yaml:[LATEST RELEASE]"
}
```

## Example file format
```yaml
version: 1.0.0

test-save: false

test-number: 6161926779561752576

test-enum: DAYS

# Section类型数据测试
user: # Section数据也支持InlineComment注释
  name: '35882'
  info:
    uuid: 554b79f1-7c39-4960-82d1-5514c9734417

uuid-value: 9a86663e-2fc7-4851-a423-c7e5d8e94a47 # This is an inline comment

sub:
  that:
    operators: []

# [ID - UUID]对照表

# 用于测试Map类型的解析与序列化保存
users:
  '1': 1c055bdd-c9d1-4931-8270-3d162247f38a
  '2': 934e2b05-2417-424e-80fd-fe58c6725837
  '3': 442949a2-8345-4210-a87b-593d7168980e
  '4': 5c015453-4b5b-42e3-ad87-b9498f2dfeab
  '5': 8f9640e7-0fbd-4f73-b737-f0b707215e71

# Inner Test
inner:
  inner-value: 51.223503560658166

class-value: 1.0

test:
  # Section类型数据测试
  user: # Section数据也支持InlineComment注释
    name: Carm
    info:
      uuid: 3d1ef2a0-a38b-44f3-b15f-8e3b22cb8cc6

# 以下内容用于测试序列化
model-test:
  some-model:
    ==: SomeModel
    num: 855
    name: 4f6b7
  any-model:
    ==: AnyModel
    name: 63d05
    state: false
  models:
    - name: 481f3
      state: true
    - name: fcf3e
      state: false
    - name: '14e50'
      state: false
  model-map:
    a:
      name: 1fb9b
      state: false
    b:
      name: 5486f
      state: false
```
