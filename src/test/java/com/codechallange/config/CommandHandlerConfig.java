package com.codechallange.config;

import com.codechallange.handler.metadataclient.DbxErrorResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan("com.codechallange.handler")
public class CommandHandlerConfig {
    @Autowired
    private DbxErrorResponseHandler errorResponseHandler;

    @Bean
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(errorResponseHandler);

        return restTemplate;
    }
}
