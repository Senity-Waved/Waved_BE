package com.senity.waved.domain.member.service;

import com.senity.waved.domain.member.dto.MemberJoinDto;
import org.springframework.security.core.userdetails.User;

import javax.security.auth.login.AccountNotFoundException;

public interface MemberService {
    String resolveRefreshToken(String refreshToken);
    void joinAfterOauth(User user, MemberJoinDto joinDto) throws AccountNotFoundException;
}

