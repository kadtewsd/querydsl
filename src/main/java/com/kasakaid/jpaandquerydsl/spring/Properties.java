package com.kasakaid.jpaandquerydsl.spring;


import org.springframework.core.env.Environment;

public class Properties {

    private static Environment environment;

    public static void setEnvironemnet(Environment env) {
        environment = env;
    }

    public static String getProperty(String key) {
        return environment.getProperty(key);
    }
}
