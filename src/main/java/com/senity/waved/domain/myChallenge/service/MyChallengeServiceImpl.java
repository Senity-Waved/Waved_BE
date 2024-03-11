package com.senity.waved.domain.myChallenge.service;

import com.senity.waved.domain.challenge.entity.Challenge;
import com.senity.waved.domain.challengeGroup.entity.ChallengeGroup;
import com.senity.waved.domain.challengeGroup.repository.ChallengeGroupRepository;
import com.senity.waved.domain.member.entity.Member;
import com.senity.waved.domain.member.repository.MemberRepository;
import com.senity.waved.domain.myChallenge.dto.response.MyChallengeResponseDto;
import com.senity.waved.domain.myChallenge.entity.MyChallenge;
import com.senity.waved.domain.myChallenge.exception.MemberNotFoundException;
import com.senity.waved.domain.myChallenge.repository.MyChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyChallengeServiceImpl implements MyChallengeService {

    private final MyChallengeRepository myChallengeRepository;
    private final MemberRepository memberRepository;
    private final ChallengeGroupRepository groupRepository;

    public void cancelAppliedMyChallenge(Long myChallengeId) {
        MyChallenge myChallenge = getMyChallengeById(myChallengeId);
        ChallengeGroup group = myChallenge.getChallengeGroup();

        myChallengeRepository.delete(myChallenge);
        group.deleteMyChallenge(myChallenge);
        groupRepository.save(group);
    }

    @Transactional(readOnly = true)
    public List<MyChallengeResponseDto> getMyChallengesInProgressListed(String email) {
        Long memberId = getMemberByEmail(email).getId();
        List<MyChallenge> inProgressListed = myChallengeRepository.findMyChallengesInProgress(memberId, LocalDate.now());

        return inProgressListed.stream()
                .map(MyChallenge::getMyChallengesInProgress)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MyChallengeResponseDto> getMyChallengesWaitingListed(String email) {
        Long memberId = getMemberByEmail(email).getId();
        List<MyChallenge> inProgressListed = myChallengeRepository.findMyChallengesWaiting(memberId, LocalDate.now());

        return inProgressListed.stream()
                .map(MyChallenge::getMyChallengesWaiting)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MyChallengeResponseDto> getMyChallengesCompletedListed(String email) {
        Long memberId = getMemberByEmail(email).getId();
        List<MyChallenge> inProgressListed = myChallengeRepository.findMyChallengesCompleted(memberId, LocalDate.now());

        return inProgressListed.stream()
                .map(MyChallenge::getMyChallengesCompleted)
                .collect(Collectors.toList());
    }

    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("해당 멤버를 찾을 수 없습니다."));
    }

    private MyChallenge getMyChallengeById(Long id) {
        return myChallengeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 마이 챌린지를 찾을 수 없습니다."));
    }
}
