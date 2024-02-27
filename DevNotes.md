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

```xml

<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
    <version>3.5.5</version>
</dependency>
```

to work with Spring Boot 3 as it states in the official documentation. (The reason it took me so long
to find it out is that there are two official websites for Mybatis Plus. One of them has all the hyperlinks
failed to load, and the right one has got many ads on it.)

## Implementation Notes

### API, URL, and Filtering

The original project uses "/front" and "/backend" as the URL for frontend resources, and I
changed it to "/customer" and "/employee" to make more sense.

```Java
@Override
protected void addResourceHandlers(ResourceHandlerRegistry registry){
        log.info("Static resources mapping");
        registry.addResourceHandler("/employee/**").addResourceLocations("classpath:/employee/");
        registry.addResourceHandler("/customer/**").addResourceLocations("classpath:/customer/");
        }
```

But this has caused some issues with the filtering of the API. The original project uses "/employee" as the base URL for
the API, which is
the same as the URL for the frontend resources.
Then the filter would behave weirdly. So I have changed it
to "/api/employee" to fix the issue and also making the API development more professional.

