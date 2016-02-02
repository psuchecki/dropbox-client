package com.codechallange.config;

import com.codechallange.handler.CommandHandler;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.codechallange.argument")
public class ArgumentPolicyTestConfig {
    @Mock
    private CommandHandler accountInfoHandler;
    @Mock
    private CommandHandler listContentHandler;
    @Mock
    private CommandHandler authorizationHandler;

    public ArgumentPolicyTestConfig() {
        MockitoAnnotations.initMocks(this);
    }

    @Bean
    public CommandHandler accountInfoHandler(){
        return accountInfoHandler;
    }

    @Bean
    public CommandHandler listContentHandler(){
        return listContentHandler;
    }

    @Bean
    public CommandHandler authorizationHandler(){
        return authorizationHandler;
    }
}