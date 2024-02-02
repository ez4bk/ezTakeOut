package com.ez4bk.eztakeout.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("Static resources mapping");
        registry.addResourceHandler("/employee/**").addResourceLocations("classpath:/employee/");
        registry.addResourceHandler("/customer/**").addResourceLocations("classpath:/customer/");
    }

}
