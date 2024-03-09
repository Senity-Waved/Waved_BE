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
import com.senity.waved.domain.verification.repository.VerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {
    private final ChallengeGroupRepository challengeGroupRepository;
    private final VerificationRepository verificationRepository;
    private final MemberRepository memberRepository;

    public void verifyChallenge(VerificationRequestDto requestDto, String email) {

        Member member = getMemberByEmail(email);
        ChallengeGroup challengeGroup = getChallengeGroup(requestDto.getChallengeGroupId());

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
                break;
            default:
                throw new IllegalArgumentException("지원하지 않는 인증 유형입니다.");
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

    private void verifyGithub(VerificationRequestDto requestDto) {
        // TODO: GITHUB 인증 처리 로직 구현
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
