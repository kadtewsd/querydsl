package com.kasakaid.jpaandquerydsl.spring;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Profile("test")
@PropertySource("classpath:application-test.properties")
@Configuration
@TestConfiguration
public class TestProfile {
}
