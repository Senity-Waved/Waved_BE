package com.senity.waved.domain.myChallenge.service;

import com.senity.waved.domain.challenge.entity.Challenge;
import com.senity.waved.domain.challenge.exception.ChallengeNotFoundException;
import com.senity.waved.domain.challenge.repository.ChallengeRepository;
import com.senity.waved.domain.challengeGroup.entity.ChallengeGroup;
import com.senity.waved.domain.challengeGroup.exception.ChallengeGroupNotFoundException;
import com.senity.waved.domain.challengeGroup.repository.ChallengeGroupRepository;
import com.senity.waved.domain.member.entity.Member;
import com.senity.waved.domain.member.exception.MemberNotFoundException;
import com.senity.waved.domain.member.repository.MemberRepository;
import com.senity.waved.domain.myChallenge.dto.response.MyChallengeCompletedDto;
import com.senity.waved.domain.myChallenge.dto.response.MyChallengeProgressDto;
import com.senity.waved.domain.myChallenge.dto.response.MyChallengeResponseDto;
import com.senity.waved.domain.myChallenge.dto.response.MyVerifsResponseDto;
import com.senity.waved.domain.myChallenge.entity.ChallengeStatus;
import com.senity.waved.domain.myChallenge.entity.MyChallenge;
import com.senity.waved.domain.myChallenge.exception.InvalidChallengeStatusException;
import com.senity.waved.domain.myChallenge.exception.MyChallengeNotFoundException;
import com.senity.waved.domain.myChallenge.repository.MyChallengeRepository;
import com.senity.waved.domain.paymentRecord.exception.MemberAndMyChallengeNotMatchException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MyChallengeServiceImpl implements MyChallengeService {

    private final MyChallengeRepository myChallengeRepository;
    private final MemberRepository memberRepository;
    private final ChallengeGroupRepository challengeGroupRepository;
    private final ChallengeRepository challengeRepository;

    @Override
    public void cancelAppliedMyChallenge(String email, Long myChallengeId) {
        Member member = getMemberByEmail(email);
        MyChallenge myChallenge = getMyChallengeById(myChallengeId);
        ChallengeGroup group = getChallengeGroupById(myChallenge.getChallengeGroupId());



        validateMember(member, myChallenge);
        group.subtractParticipantCount();
        myChallengeRepository.delete(myChallenge);
    }

    @Override
    public List<MyChallengeResponseDto> getMyChallengesListed(String email, ChallengeStatus status) {
        Member member = getMemberByEmail(email);
        List<MyChallenge> myChallengesListed;
        ZonedDateTime todayStart = ZonedDateTime.now(ZoneId.of("GMT")).truncatedTo(ChronoUnit.DAYS);

        switch (status) {
            case PROGRESS:
                myChallengesListed = myChallengeRepository.findMyChallengesInProgressAndIsPaidTrue(member.getId(), todayStart);
                break;
            case WAITING:
                myChallengesListed = myChallengeRepository.findMyChallengesWaitingAndIsPaidTrue(member.getId(), todayStart);
                break;
            case COMPLETED:
                myChallengesListed = myChallengeRepository.findMyChallengesCompletedAndIsPaidTrue(member.getId(), todayStart);
                break;
            default:
                throw new InvalidChallengeStatusException("유효하지 않은 챌린지 상태 입니다.");
        }

        log.error("--------------------------todayStart : " + todayStart);
        if(!myChallengesListed.isEmpty()) {
            log.error("---------------myChallenge startDate : " + myChallengesListed.get(0).getStartDate());
            if(myChallengesListed.get(0).getStartDate().equals(todayStart)) {
                log.error("------------날짜 비교 테스트 성공 GMT");
            }
        }

        return myChallengesListed.stream()
                .map(myChallenge -> mapToResponseDto(myChallenge, status, member))
                .collect(Collectors.toList());
    }

    @Override
    public MyVerifsResponseDto getMyVerifications(Long myChallengeId) {
        MyChallenge myChallenge = getMyChallengeById(myChallengeId);
        ChallengeGroup group = getChallengeGroupById(myChallenge.getChallengeGroupId());
        return new MyVerifsResponseDto(myChallenge, group);
    }

    private MyChallengeResponseDto mapToResponseDto(MyChallenge myChallenge, ChallengeStatus status, Member member) {
        Boolean isGithubConnected = member.isGithubConnected();
        ChallengeGroup group = getChallengeGroupById(myChallenge.getChallengeGroupId());
        Challenge challenge = getChallengeById(group.getChallengeId());

        switch (status) {
            case PROGRESS:
                return MyChallengeProgressDto.of(myChallenge, group, challenge, isGithubConnected);
            case WAITING:
                return MyChallengeResponseDto.of(myChallenge, group);
            case COMPLETED:
                return MyChallengeCompletedDto.of(myChallenge, group, challenge);
            default:
                throw new InvalidChallengeStatusException("유효하지 않은 챌린지 상태 입니다.");
        }
    }

    private Challenge getChallengeById(Long id) {
        return challengeRepository.findById(id)
                .orElseThrow(() -> new ChallengeNotFoundException("해당 챌린지를 찾을 수 없습니다."));
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

    private void validateMember(Member member, MyChallenge myChallenge) {
        if(!myChallenge.getMemberId().equals(member.getId())) {
            throw new MemberAndMyChallengeNotMatchException("해당 멤버의 마이 챌린지가 아닙니다.");
        }
    }
}
