# Developer's Notes

## Environment Config

The original project was developed with Java 1.8 and Spring Boot 2.4.5,
which is outdated. I updated the project to use:

- Java 21
- Spring Boot 3.2.2

This has caused some issues during my development, but I have managed to resolve them.

The first problem I encountered was with the "Mybatis Plus" plugin. The original project was
using Spring Boot 2 stating in the pom.xml file that the plugin name is
"mybatis-plus-spring-boot-starter". But it would lead to UnsatisfiedDependencyException
with error creating beans in Spring Boot 3. It turns out that the plugin should be:

```
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
    <version>3.5.5</version>
</dependency>
```

to work with Spring Boot 3 as it states in the official documentation. (The reason it took me so long
to find it out is that there are two official websites for Mybatis Plus. One of them has all the hyperlinks
failed to load, and the right one has got many ads on it.)