package com.conductor.livetocmsproxy.controller.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public RestTemplateBuilder restTemplate(){
        return new RestTemplateBuilder();
    }
}
