package com.senity.waved.base.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisTemplate<String, Object> redisBlackListTemplate;

    public void deleteByEmail(String email) {
        redisTemplate.delete(email);
    }

    public void save(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void setBlackList(String key, Object o, int minutes) {
        redisBlackListTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(o.getClass()));
        redisBlackListTemplate.opsForValue().set(key.substring(7), o, minutes, TimeUnit.MINUTES);
    }

    public boolean hasKeyBlackList(String key) {
        return Boolean.TRUE.equals(redisBlackListTemplate.hasKey(key));
    }

    public Optional<Redis> findByEmail(String email) {
        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
        String refreshToken = valueOperations.get(email);

        if (Objects.isNull(refreshToken)) {
            return Optional.empty();
        }

        return Optional.of(new Redis(email, refreshToken));
    }
}
