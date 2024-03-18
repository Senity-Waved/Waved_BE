package com.senity.waved.base.config;

import com.senity.waved.base.redis.RedisProperties;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import io.lettuce.core.resource.DefaultClientResources;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@RequiredArgsConstructor
@Configuration
@EnableRedisRepositories
public class RedisConfig {

    private final RedisProperties redisProperties;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .clientOptions(ClientOptions.builder()
                        .socketOptions(SocketOptions.builder()
                                .keepAlive(true)
                                .build())
                        .build())
                .clientResources(DefaultClientResources.create())
                .build();

        RedisConfiguration redisConfig = new RedisStandaloneConfiguration(
                redisProperties.getHost(), redisProperties.getPort()
        );
        return new LettuceConnectionFactory(redisConfig, clientConfig);
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
