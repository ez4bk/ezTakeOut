package com.ez4bk.eztakeout;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@Slf4j
@SpringBootApplication
@ServletComponentScan
public class EzTakeOutApplication {

    public static void main(String[] args) {
        SpringApplication.run(EzTakeOutApplication.class, args);
        log.info("EzTakeOutApplication started successfully");
    }

}
