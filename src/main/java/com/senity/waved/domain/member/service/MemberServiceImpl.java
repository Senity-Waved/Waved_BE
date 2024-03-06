package com.senity.waved.domain.member.service;

import com.senity.waved.base.redis.RedisUtil;
import com.senity.waved.domain.member.dto.MemberJoinDto;
import com.senity.waved.domain.member.entity.Member;
import com.senity.waved.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final RedisUtil redisUtil;

    @Transactional(readOnly = true)
    public String resolveRefreshToken(String refreshToken) {
        if (refreshToken == null || !refreshToken.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "리프레시 토큰이 누락되었거나 올바르지 않습니다.");
        }
        return refreshToken.substring(7);
    }

    @Transactional
    public void joinAfterOauth(User user, MemberJoinDto joinDto) throws AccountNotFoundException {
        String email = user.getUsername();
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        Member member = optionalMember.orElseThrow(AccountNotFoundException::new);
        member.updateInfo(joinDto);
        memberRepository.save(member);
    }

    @Transactional
    public void logout(String token, String email) {
        redisUtil.setBlackList(token, "accessToken", 10);
        redisUtil.deleteByEmail(email);
    }

    @Transactional
    public void deleteMember(String email) {
        memberRepository.deleteByEmail(email);
        redisUtil.deleteByEmail(email);
    }
}
