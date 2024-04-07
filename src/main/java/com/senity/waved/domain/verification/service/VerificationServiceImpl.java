package com.senity.waved.domain.verification.service;

import com.senity.waved.domain.challenge.entity.Challenge;
import com.senity.waved.domain.challenge.entity.VerificationType;
import com.senity.waved.domain.challenge.exception.ChallengeNotFoundException;
import com.senity.waved.domain.challenge.repository.ChallengeRepository;
import com.senity.waved.domain.challengeGroup.entity.ChallengeGroup;
import com.senity.waved.domain.challengeGroup.exception.ChallengeGroupNotFoundException;
import com.senity.waved.domain.challengeGroup.repository.ChallengeGroupRepository;
import com.senity.waved.domain.member.entity.Member;
import com.senity.waved.domain.member.exception.MemberNotFoundException;
import com.senity.waved.domain.member.repository.MemberRepository;
import com.senity.waved.domain.myChallenge.entity.MyChallenge;
import com.senity.waved.domain.myChallenge.exception.MyChallengeNotFoundException;
import com.senity.waved.domain.myChallenge.repository.MyChallengeRepository;
import com.senity.waved.domain.verification.dto.request.VerificationRequestDto;
import com.senity.waved.domain.verification.entity.Verification;
import com.senity.waved.domain.verification.exception.AlreadyVerifiedException;
import com.senity.waved.domain.verification.exception.NoVerificationFieldException;
import com.senity.waved.domain.verification.exception.UnsupportedAuthenticationTypeException;
import com.senity.waved.domain.verification.exception.VerificationNotTextException;
import com.senity.waved.domain.verification.repository.VerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@Service
@Transactional
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {

    private final ChallengeGroupRepository challengeGroupRepository;
    private final ChallengeRepository challengeRepository;
    private final VerificationRepository verificationRepository;
    private final MemberRepository memberRepository;
    private final GithubService githubService;
    private final MyChallengeRepository myChallengeRepository;
    private final AzureBlobStorageService azureBlobStorageService;

    @Override
    public void verifyChallenge(VerificationRequestDto requestDto, String email, Long challengeGroupId) {
        Member member = getMemberByEmail(email);
        ChallengeGroup challengeGroup = getChallengeGroup(challengeGroupId);
        verifyMyChallenge(member, challengeGroup);

        Challenge challenge = getChallengeById(challengeGroup.getChallengeId());
        VerificationType verificationType = challenge.getVerificationType();
        boolean isSuccess = false;

        switch (verificationType) {
            case TEXT:
                isSuccess = verifyText(requestDto, member, challengeGroup);
                break;
            case LINK:
                isSuccess = verifyLink(requestDto, member, challengeGroup);
                break;
            case PICTURE:
                isSuccess = verifyPicture(requestDto, member, challengeGroup);
                break;
            case GITHUB:
                isSuccess = verifyGithub(member, challengeGroup);
                break;
            default:
                throw new UnsupportedAuthenticationTypeException("지원하지 않는 인증 유형입니다.");
        }
        updateMyChallengeStatus(member, challengeGroup, isSuccess);
    }

    @Override
    public void IsChallengeGroupTextType(Long challengeGroupId) {
        ChallengeGroup challengeGroup = getChallengeGroup(challengeGroupId);
        Challenge challenge = getChallengeById(challengeGroup.getChallengeId());

        if (challenge.getVerificationType() != VerificationType.TEXT) {
            throw new VerificationNotTextException("이 챌린지는 글 인증이 아닙니다.");
        }
    }

    private boolean verifyText(VerificationRequestDto requestDto, Member member, ChallengeGroup challengeGroup) {
        validateRequestDto(requestDto);

        Verification verification = Verification.of(requestDto, member.getId(), challengeGroup.getId(), null, VerificationType.TEXT);
        verificationRepository.save(verification);
        return true;
    }

    private boolean verifyLink(VerificationRequestDto requestDto, Member member, ChallengeGroup challengeGroup) {
        validateRequestDto(requestDto);

        if (requestDto.getLink() == null || requestDto.getLink().isEmpty()) {
            throw new NoVerificationFieldException("링크를 입력해주세요.");
        }

        Verification verification = Verification.of(requestDto, member.getId(), challengeGroup.getId(), null, VerificationType.LINK);
        verificationRepository.save(verification);
        return true;
    }

    public boolean verifyGithub(Member member, ChallengeGroup challengeGroup) {
        try {
            boolean hasCommitsToday = githubService.hasCommitsToday(member.getGithubId(), member.getGithubToken());
            Verification verification = Verification.createGithubVerification(member, challengeGroup, hasCommitsToday);

            verificationRepository.save(verification);
            return hasCommitsToday;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("GitHub API 호출 중 오류 발생: " + e.getMessage());
        }
    }

    private boolean verifyPicture(VerificationRequestDto requestDto, Member member, ChallengeGroup challengeGroup) {
        if (requestDto.getImageUrl() == null || requestDto.getImageUrl().isEmpty()) {
            throw new NoVerificationFieldException("이미지 URL을 입력해주세요.");
        }
        try {
            byte[] pictureData = requestDto.getImageUrl().getBytes();
            String fileName = member.getId() + "_" + System.currentTimeMillis();
            String imageUrl = azureBlobStorageService.uploadPicture(pictureData, fileName);

            Verification verification = Verification.of(requestDto, member.getId(), challengeGroup.getId(), imageUrl, VerificationType.PICTURE);
            verificationRepository.save(verification);
            return true;
        } catch (IOException e) {
            throw new RuntimeException("사진 데이터 변환 중 오류가 발생했습니다.", e);
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

    private void validateRequestDto(VerificationRequestDto requestDto) {
        if (requestDto == null || requestDto.getContent() == null || requestDto.getContent().isEmpty()) {
            throw new NoVerificationFieldException("내용을 입력해주세요.");
        }
    }

    // 제출안함(0), 실패(1), 성공(2)
    private void updateMyChallengeStatus(Member member, ChallengeGroup challengeGroup, boolean isSuccess) {
        ZonedDateTime currentDate = ZonedDateTime.now();

        MyChallenge myChallenge = getMyChallengeByMemberAndGroup(member, challengeGroup);

        if (myChallenge.isValidChallengePeriod(challengeGroup.getStartDate(), currentDate)) {
            updateVerificationAndSuccessCount(myChallenge, challengeGroup.getStartDate(), currentDate, isSuccess);
            myChallengeRepository.save(myChallenge);
        }
    }

    private MyChallenge getMyChallengeByMemberAndGroup(Member member, ChallengeGroup challengeGroup) {
        return myChallengeRepository.findByMemberIdAndChallengeGroupIdAndIsPaidTrue(member.getId(), challengeGroup.getId())
                .orElseThrow(() -> new MyChallengeNotFoundException("해당 마이 챌린지를 찾을 수 없습니다."));
    }

    private Challenge getChallengeById(Long id) {
        return challengeRepository.findById(id)
                .orElseThrow(() -> new ChallengeNotFoundException("해당 챌린지를 찾을 수 없습니다."));
    }

    private void updateVerificationAndSuccessCount(MyChallenge myChallenge, ZonedDateTime startDate, ZonedDateTime currentDate, boolean isSuccess) {
        long daysFromStart = ChronoUnit.DAYS.between(startDate, currentDate);

        if (!myChallenge.isVerified()) {
            myChallenge.updateVerificationStatus((int)(daysFromStart) + 1, isSuccess);
        } else {
            throw new AlreadyVerifiedException("이미 오늘의 인증을 완료했습니다.");
        }

        if (isSuccess) {
            myChallenge.incrementSuccessCount();
        }
    }

    private void verifyMyChallenge(Member member, ChallengeGroup challengeGroup) {
        MyChallenge myChallenge = getMyChallengeByMemberAndGroup(member, challengeGroup);
        myChallenge.verify();
    }
}