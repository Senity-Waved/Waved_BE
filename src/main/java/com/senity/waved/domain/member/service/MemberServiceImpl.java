package com.senity.waved.domain.member.service;

import com.senity.waved.base.redis.RedisUtil;
import com.senity.waved.domain.member.dto.GithubInfoDto;
import com.senity.waved.domain.member.dto.MemberJoinDto;
import com.senity.waved.domain.member.dto.ProfileEditDto;
import com.senity.waved.domain.member.dto.ProfileInfoDto;
import com.senity.waved.domain.member.entity.Member;
import com.senity.waved.domain.member.repository.MemberRepository;
import com.senity.waved.domain.myChallenge.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.security.auth.login.AccountNotFoundException;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final RedisUtil redisUtil;
    private GitHub github;

    @Transactional(readOnly = true)
    public String resolveRefreshToken(String refreshToken) {
        if (refreshToken == null || !refreshToken.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "리프레시 토큰이 누락되었거나 올바르지 않습니다.");
        }
        return refreshToken.substring(7);
    }

    @Transactional
    public void joinAfterOauth(String email, MemberJoinDto joinDto) {
        Member member = getMemberByEmail(email);
        member.updateInfo(joinDto);
        memberRepository.save(member);
    }

    @Transactional
    public void logout(String email, String token) {
        redisUtil.setBlackList(token, "accessToken", 10);
        redisUtil.deleteByEmail(email);
    }

    @Transactional
    public void deleteMember(String email) {
        memberRepository.deleteByEmail(email);
        redisUtil.deleteByEmail(email);
    }

    @Transactional
    public ProfileInfoDto getProfileInfo(String email) {
        Member member = getMemberByEmail(email);
        return ProfileInfoDto.builder()
                .nickname(member.getNickname())
                .jobTitle(member.getJobTitle())
                .githubId(member.getGithubId())
                .build();
    }

    public ProfileEditDto getProfileInfoToEdit(String email) {
        Member member = getMemberByEmail(email);
        return ProfileEditDto.builder()
                .nickname(member.getNickname())
                .jobTitle(member.getJobTitle())
                .birthYear(member.getBirthYear())
                .gender(member.getGender())
                .build();
    }

    @Transactional(readOnly = true)
    public GithubInfoDto getGithubInfoToEdit(String email) {
        Member member = getMemberByEmail(email);
        return GithubInfoDto.builder()
                .githubId(member.getGithubId())
                .githubToken(member.getGithubToken())
                .build();
    }

    @Transactional
    public void checkGithubConnection(String email, GithubInfoDto githubDto) {
        GHUser ghUser = checkCredentials(githubDto);
        if (ghUser == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않은 github 정보입니다.");
        }
        saveGithubInfo(email, githubDto);
    }

    @Transactional
    public void saveGithubInfo(String email, GithubInfoDto githubDto) {
        Member member = getMemberByEmail(email);
        member.updateGithubInfo(githubDto, true);
    }

    @Transactional
    public void deleteGithubInfo(String email) {
        Member member = getMemberByEmail(email);
        member.updateGithubInfo(GithubInfoDto.builder().build(), false);
    }

    private GHUser checkCredentials(GithubInfoDto githubDto) {
        try {
            github = new GitHubBuilder().withOAuthToken(githubDto.getGithubToken()).build();
            github.checkApiUrlValidity();
            return github.getUser(githubDto.getGithubId());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다."));
    }
}
