package com.basyir.security.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityAppConfig {

    @Bean
    ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
