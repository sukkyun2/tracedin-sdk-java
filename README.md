# Tracedin SDK for Java

![GitHub](https://img.shields.io/github/license/tracedin/tracedin-sdk-java)
![Maven Central](https://img.shields.io/maven-central/v/io.github.tracedin/tracedin-sdk)
## Setup

### Java Version

The current release of SDK Java supports Java 17+.

### Add a dependency on the Tracedin Java SDK to your project

If you're using Maven, then edit your project's "pom.xml" and add this to the `<dependencies>` section:

```xml
<dependency>
    <groupId>io.github.tracedin</groupId>
    <artifactId>tracedin-sdk</artifactId>
    <version>0.0.2</version>
</dependency>
```

If you are using Gradle, then edit your project's "build.gradle" and add this to the `dependencies` section:

```groovy
dependencies {
    // ...
    implementation 'io.github.tracedin:tracedin-sdk:0.0.2'
}
```

## Usage

Please add `CollectLoggingInterceptor`

```java
package io.github.tracedin.config;

import io.github.tracedin.web.CollectLoggingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CollectLoggingInterceptor());
    }
}
```

