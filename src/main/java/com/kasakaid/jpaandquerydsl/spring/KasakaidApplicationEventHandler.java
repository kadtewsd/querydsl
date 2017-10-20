package com.kasakaid.jpaandquerydsl.spring;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KasakaidApplicationEventHandler implements ApplicationListener<ApplicationReadyEvent> {
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("we set environment object...");
        Properties.setEnvironemnet(event.getApplicationContext().getEnvironment());
    }

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        log.info("context refreshed");
    }
}

