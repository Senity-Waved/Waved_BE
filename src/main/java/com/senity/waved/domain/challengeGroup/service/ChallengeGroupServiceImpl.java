package com.senity.waved.domain.challengeGroup.service;

import com.senity.waved.domain.challengeGroup.dto.response.ChallengeGroupResponseDto;
import com.senity.waved.domain.challengeGroup.dto.response.VerificationListResponseDto;

import com.senity.waved.domain.challengeGroup.entity.ChallengeGroup;
import com.senity.waved.domain.challengeGroup.exception.ChallengeGroupNotFoundException;
import com.senity.waved.domain.challengeGroup.repository.ChallengeGroupRepository;
import com.senity.waved.domain.verification.entity.Verification;
import com.senity.waved.domain.verification.repository.VerificationRepository;
import com.senity.waved.domain.member.entity.Member;
import com.senity.waved.domain.member.repository.MemberRepository;
import com.senity.waved.domain.myChallenge.entity.MyChallenge;
import com.senity.waved.domain.myChallenge.exception.AlreadyMyChallengeExistsException;
import com.senity.waved.domain.myChallenge.exception.MemberNotFoundException;
import com.senity.waved.domain.myChallenge.repository.MyChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Optional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChallengeGroupServiceImpl implements ChallengeGroupService {

    private final MemberRepository memberRepository;
    private final MyChallengeRepository myChallengeRepository;
    private final VerificationRepository verificationRepository;
    private final ChallengeGroupRepository challengeGroupRepository;


    public void applyForChallengeGroup(String email, Long groupId) {
        Member member = getMemberByEmail(email);
        ChallengeGroup group = getGroupById(groupId);

        Optional<MyChallenge> myChallenge = myChallengeRepository.findByMemberAndChallengeGroup(member, group);
        if (myChallenge.isPresent()) {
            throw new AlreadyMyChallengeExistsException("이미 신청되어있는 챌린지 그룹 입니다.");
        }

        MyChallenge newMyChallenge = MyChallenge.builder()
                .challengeGroup(group)
                .successCount(0L)
                .isReviewed(false)
                .member(member)
                .myVerifs(new int[14])
                .build();

        myChallengeRepository.save(newMyChallenge);
        group.addMyChallenge(newMyChallenge);
        challengeGroupRepository.save(group);
    }

    public ChallengeGroupResponseDto getGroupDetail(String email, Long groupId) {
        ChallengeGroup group = getGroupById(groupId);
        Member member = getMemberByEmail(email);
        Optional<MyChallenge> myChallenge = myChallengeRepository.findByMemberAndChallengeGroup(member, group);

        Boolean isApplied = myChallenge.isPresent()? true : false;
        return ChallengeGroup.getGroupResponse(group, isApplied);
    }
  
    @Override
    public List<VerificationListResponseDto> getVerifications(Long challengeGroupId, Timestamp verificationDate) {
        ChallengeGroup challengeGroup = findChallengeGroupById(challengeGroupId);
        LocalDateTime[] dateRange = calculateStartAndEndDate(verificationDate);
        List<Verification> verifications = findVerifications(challengeGroup, dateRange);
        return convertToDtoList(verifications);
    }

    private ChallengeGroup getGroupById(Long id) {
        return challengeGroupRepository.findById(id)
                .orElseThrow(() -> new ChallengeGroupNotFoundException("해당 챌린지 그룹을 찾을 수 없습니다."));
    }

    private Member getMemberByEmail(String email) {
        return memberRepository.getMemberByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("해당 회원을 찾을 수 없습니다."));
    }

    private ChallengeGroup findChallengeGroupById(Long challengeGroupId) {
        return challengeGroupRepository.findById(challengeGroupId)
                .orElseThrow(() -> new ChallengeGroupNotFoundException("챌린지 기수를 찾을 수 없습니다."));
    }

    private LocalDateTime[] calculateStartAndEndDate(Timestamp verificationDate) {
        LocalDateTime startOfDay = verificationDate.toLocalDateTime().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        return new LocalDateTime[]{startOfDay, endOfDay};
    }

    private List<Verification> findVerifications(ChallengeGroup challengeGroup, LocalDateTime[] dateRange) {
        return verificationRepository.findByCreateDateBetweenAndChallengeGroup(
                Timestamp.valueOf(dateRange[0]), Timestamp.valueOf(dateRange[1]), challengeGroup);
    }

    private List<VerificationListResponseDto> convertToDtoList(List<Verification> verifications) {
        if (verifications.isEmpty()) {
            throw new IllegalArgumentException("해당 날짜에 존재하는 인증내역이 없습니다.");
        }
        return verifications.stream()
                .map(VerificationListResponseDto::new)
                .collect(Collectors.toList());
    }
}
