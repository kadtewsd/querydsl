package com.kasakaid.jpaandquerydsl.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;


//@Configuration
@TestConfiguration
@ComponentScan(basePackages = {"com.kasakaid.myboot"})
@Configuration
public class TestConfig {

    @Autowired
    Environment environment;

    @Bean
    public TransactionAwareDataSourceProxy dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        // Autowired した environemnent からキーが発見できない。。
        // log4jdbc が何故かロードできない。
//        dataSource.setDriverClassName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy");
        dataSource.setDriverClassName(environment.getProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(environment.getProperty("spring.datasource.url"));
        dataSource.setUsername(environment.getProperty("spring.datasource.username"));
        dataSource.setPassword(environment.getProperty("spring.datasource.password"));

        return new TransactionAwareDataSourceProxy(dataSource);
    }

    @Bean
    public MyResource myResource() {
        return new MyResource();
    }
}

