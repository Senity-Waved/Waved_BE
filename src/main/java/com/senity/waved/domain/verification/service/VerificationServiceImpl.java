package com.senity.waved.domain.verification.service;

import com.senity.waved.domain.challenge.entity.Challenge;
import com.senity.waved.domain.challenge.entity.VerificationType;
import com.senity.waved.domain.challengeGroup.entity.ChallengeGroup;
import com.senity.waved.domain.challengeGroup.exception.ChallengeGroupNotFoundException;
import com.senity.waved.domain.challengeGroup.repository.ChallengeGroupRepository;
import com.senity.waved.domain.member.entity.Member;
import com.senity.waved.domain.member.repository.MemberRepository;
import com.senity.waved.domain.myChallenge.exception.MemberNotFoundException;
import com.senity.waved.domain.verification.dto.request.VerificationRequestDto;
import com.senity.waved.domain.verification.entity.Verification;
import com.senity.waved.domain.verification.exception.ChallengeGroupVerificationException;
import com.senity.waved.domain.verification.repository.VerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {

    private final ChallengeGroupRepository challengeGroupRepository;
    private final VerificationRepository verificationRepository;
    private final MemberRepository memberRepository;
    private final GithubService githubService;

    public void verifyChallenge(VerificationRequestDto requestDto, String email, Long challengeGroupId) {

        Member member = getMemberByEmail(email);
        ChallengeGroup challengeGroup = getChallengeGroup(challengeGroupId);

        Challenge challenge = challengeGroup.getChallenge();
        VerificationType verificationType = challenge.getVerificationType();

        switch (verificationType) {
            case TEXT:
            case LINK:
                verifyTextOrLink(requestDto, member, challengeGroup, verificationType);
                break;
            case PICTURE:
                break;
            case GITHUB:
                verifyGithub(requestDto, member, challengeGroup, challengeGroupId);
                break;
            default:
                throw new IllegalArgumentException("지원하지 않는 인증 유형입니다.");
        }
    }

    public void challengeGroupIsTextType(Long challengeGroupId) throws ChallengeGroupVerificationException {
        ChallengeGroup challengeGroup = getChallengeGroup(challengeGroupId);

        if (challengeGroup.getChallenge().getVerificationType() != VerificationType.TEXT) {
            throw new ChallengeGroupVerificationException("이 챌린지는 글 인증이 아닙니다.");
        }
    }

    private void verifyTextOrLink(VerificationRequestDto requestDto, Member member, ChallengeGroup challengeGroup, VerificationType verificationType) {
        Verification verification = Verification.builder()
                .content(requestDto.getContent())
                .member(member)
                .challengeGroup(challengeGroup)
                .verificationType(verificationType)
                .build();
        verificationRepository.save(verification);
    }

    private void verifyPicture(VerificationRequestDto requestDto) {
        // TODO: PICTURE 인증 처리 로직 구현
    }

    public void verifyGithub(VerificationRequestDto requestDto, Member member, ChallengeGroup challengeGroup, Long challengeGroupId) {

        try {
            boolean hasCommitsToday = githubService.hasCommitsToday(member.getGithubId(), member.getGithubToken());

            Verification verification = Verification.createGithubVerification(member, challengeGroup, hasCommitsToday);
            verificationRepository.save(verification);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("GitHub API 호출 중 오류 발생: " + e.getMessage());
        }
    }

    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다."));
    }

    private ChallengeGroup getChallengeGroup(Long challengeGroupId) {
        return challengeGroupRepository.findById(challengeGroupId)
                .orElseThrow(() -> new ChallengeGroupNotFoundException("챌린지 기수를 찾을 수 없습니다."));
    }
}