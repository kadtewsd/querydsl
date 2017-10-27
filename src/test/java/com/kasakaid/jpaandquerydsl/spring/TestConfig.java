package com.kasakaid.jpaandquerydsl.spring;

import com.kasakaid.jpaandquerydsl.ApplicationTest;
import lombok.SneakyThrows;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;


//@Configuration
@TestConfiguration
@ComponentScan(basePackageClasses={ApplicationTest.class})
public class TestConfig {

    @Autowired
    Environment environment;


    @Bean
    public TransactionAwareDataSourceProxy dataSource() {
        net.sf.log4jdbc.sql.jdbcapi.DataSourceSpy proxyDs = null;
        if ("jdbc:log4jdbc:h2:mem:test;MODE=MySQL;DB_CLOSE_ON_EXIT=FALSE".equals(environment.getProperty("spring.datasource.url"))) {
            EmbeddedDatabase ds = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
            proxyDs = new net.sf.log4jdbc.sql.jdbcapi.DataSourceSpy(ds);
            return new TransactionAwareDataSourceProxy(proxyDs);
        }
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        proxyDs = new net.sf.log4jdbc.sql.jdbcapi.DataSourceSpy(dataSource);
        dataSource.setDriverClassName("net.sf.log4jdbc.DriverSpy");
        dataSource.setUrl("jdbc:log4jdbc:mysql://localhost:3306/testdb");
        dataSource.setUsername("root");
        dataSource.setPassword("mysql");
        return new TransactionAwareDataSourceProxy(proxyDs);
    }

    @Bean
    public MyResource myResource() {
        return new MyResource();
    }

//    @SneakyThrows
//    @Bean(name = "h2WebServer", initMethod = "start", destroyMethod = "stop")
//    public org.h2.tools.Server h2WebServer() {
//         return Server.createWebServer("-web", "-webAllowOthers", "-webPort", "9100");
//    }
//
//    @SneakyThrows
//    @Bean(initMethod = "start", destroyMethod = "stop")
//    @DependsOn(value = "h2WebServer")
//    public org.h2.tools.Server h2Server() {
//        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
//    }
}

