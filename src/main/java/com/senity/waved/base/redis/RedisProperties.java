package com.senity.waved.base.redis;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Getter
@PropertySource("application-secret.yml")
public class RedisProperties {
    @Value("${custom.redis.port}")
    private int port;
    @Value("${custom.redis.host}")
    private String host;
}