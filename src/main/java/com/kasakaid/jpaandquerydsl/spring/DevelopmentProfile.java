package com.kasakaid.jpaandquerydsl.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Profile("development")
@PropertySource("classpath:application-development.properties")
@Configuration
public class DevelopmentProfile {
}
