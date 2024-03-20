package com.senity.waved.base.config;

import com.siot.IamportRestClient.IamportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IamportClientConfig {

    @Value("${iamPort.rest.api.key}")
    private String key;

    @Value("${iamPort.rest.api.secret}")
    private String secret;


    @Bean
    public IamportClient iamportClient() {
        return new IamportClient(key, secret);
    }
}

