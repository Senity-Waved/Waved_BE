package com.senity.waved.domain.myChallenge.service;

import com.senity.waved.domain.challengeGroup.entity.ChallengeGroup;
import com.senity.waved.domain.challengeGroup.exception.ChallengeGroupNotFoundException;
import com.senity.waved.domain.challengeGroup.repository.ChallengeGroupRepository;
import com.senity.waved.domain.member.entity.Member;
import com.senity.waved.domain.member.exception.MemberNotFoundException;
import com.senity.waved.domain.member.repository.MemberRepository;
import com.senity.waved.domain.myChallenge.dto.response.MyChallengeResponseDto;
import com.senity.waved.domain.myChallenge.dto.response.MyVerifsResponseDto;
import com.senity.waved.domain.myChallenge.entity.ChallengeStatus;
import com.senity.waved.domain.myChallenge.entity.MyChallenge;
import com.senity.waved.domain.myChallenge.exception.MyChallengeNotFoundException;
import com.senity.waved.domain.myChallenge.repository.MyChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MyChallengeServiceImpl implements MyChallengeService {

    private final MyChallengeRepository myChallengeRepository;
    private final MemberRepository memberRepository;
    private final ChallengeGroupRepository challengeGroupRepository;

    @Transactional
    public void cancelAppliedMyChallenge(Long myChallengeId) {
        MyChallenge myChallenge = getMyChallengeById(myChallengeId);
        ChallengeGroup group = getChallengeGroupById(myChallenge.getChallengeGroupId());

        myChallengeRepository.delete(myChallenge);
        group.deleteMyChallenge(myChallenge);
        challengeGroupRepository.save(group);
    }

    @Transactional
    public List<MyChallengeResponseDto> getMyChallengesListed(String email, ChallengeStatus status) {
        Member member = getMemberByEmail(email);
        List<MyChallenge> myChallengesListed;
        ZonedDateTime todayStart = ZonedDateTime.now().toLocalDate().atStartOfDay(ZoneId.systemDefault());

        switch (status) {
            case PROGRESS:
                myChallengesListed = myChallengeRepository.findMyChallengesInProgress(member.getId(), todayStart);
                break;
            case WAITING:
                myChallengesListed = myChallengeRepository.findMyChallengesWaiting(member.getId(), todayStart);
                break;
            case COMPLETED:
                myChallengesListed = myChallengeRepository.findMyChallengesCompleted(member.getId(), todayStart);
                break;
            default:
                throw new IllegalArgumentException("유효하지 않은 챌린지 상태 입니다.");
        }

        return myChallengesListed.stream()
                .map(myChallenge -> mapToResponseDto(myChallenge, status, member))
                .collect(Collectors.toList());
    }

    public MyVerifsResponseDto getMyVerifications(Long myChallengeId) {
        MyChallenge myChallenge = getMyChallengeById(myChallengeId);
        ChallengeGroup group = getChallengeGroupById(myChallenge.getChallengeGroupId());
        return new MyVerifsResponseDto(myChallenge, group);
    }

    private MyChallengeResponseDto mapToResponseDto(MyChallenge myChallenge, ChallengeStatus status, Member member) {
        boolean isGithubConnected = member.isGithubConnected();
        ChallengeGroup group = getChallengeGroupById(myChallenge.getChallengeGroupId());

        switch (status) {
            case PROGRESS:
                return myChallenge.getMyChallengesInProgress(myChallenge, group, isGithubConnected);
            case WAITING:
                return myChallenge.getMyChallengesWaiting(myChallenge, group);
            case COMPLETED:
                return myChallenge.getMyChallengesCompleted(myChallenge, group);
            default:
                throw new IllegalArgumentException("유효하지 않은 챌린지 상태 입니다.");
        }
    }

    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("해당 멤버를 찾을 수 없습니다."));
    }

    private MyChallenge getMyChallengeById(Long id) {
        return myChallengeRepository.findById(id)
                .orElseThrow(() -> new MyChallengeNotFoundException("해당 마이 챌린지를 찾을 수 없습니다."));
    }

    private ChallengeGroup getChallengeGroupById(Long id) {
        return challengeGroupRepository.findById(id)
                .orElseThrow(() -> new ChallengeGroupNotFoundException("해당 챌린지 그룹을 찾을 수 없습니다."));
    }
}
