package com.senity.waved.domain.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthLevel {

    MEMBER(3, "ROLE_USER"),
    ADMIN(7, "ROLE_ADMIN");

    private final Integer code;
    private final String value;
}
