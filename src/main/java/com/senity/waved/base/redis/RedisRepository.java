package com.senity.waved.base.redis;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RedisRepository extends CrudRepository<Redis, String> {
    Optional<Redis> findRedisByRefreshToken(String refreshToken);
    Boolean existsByRefreshToken(String refreshToken);
    Boolean existsRedisByEmail(String email);

    Boolean deleteByEmail(String email);
}
