package com.kasakaid.jpaandquerydsl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@SpringBootApplication(scanBasePackageClasses = Application.class)
@EntityScan(basePackageClasses = {Application.class, Jsr310JpaConverters.class})
@Slf4j
public class Application {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Application.class);
        log.info(applicationContext.getApplicationName());
        SpringApplication.run(Application.class);
    }
}
