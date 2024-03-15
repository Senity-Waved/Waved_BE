package com.senity.waved.domain.myChallenge.service;

import com.senity.waved.domain.challengeGroup.entity.ChallengeGroup;
import com.senity.waved.domain.challengeGroup.repository.ChallengeGroupRepository;
import com.senity.waved.domain.member.entity.Member;
import com.senity.waved.domain.member.repository.MemberRepository;
import com.senity.waved.domain.myChallenge.dto.response.MyChallengeResponseDto;
import com.senity.waved.domain.myChallenge.dto.response.MyVerifsResponseDto;
import com.senity.waved.domain.myChallenge.entity.ChallengeStatus;
import com.senity.waved.domain.myChallenge.entity.MyChallenge;
import com.senity.waved.domain.myChallenge.exception.MemberNotFoundException;
import com.senity.waved.domain.myChallenge.exception.MyChallengeNotFoundException;
import com.senity.waved.domain.myChallenge.repository.MyChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MyChallengeServiceImpl implements MyChallengeService {

    private final MyChallengeRepository myChallengeRepository;
    private final MemberRepository memberRepository;
    private final ChallengeGroupRepository challengeGroupRepository;

    public void cancelAppliedMyChallenge(Long myChallengeId) {
        MyChallenge myChallenge = getMyChallengeById(myChallengeId);
        ChallengeGroup group = myChallenge.getChallengeGroup();

        myChallengeRepository.delete(myChallenge);
        group.deleteMyChallenge(myChallenge);
        challengeGroupRepository.save(group);
    }

    @Transactional(readOnly = true)
    public List<MyChallengeResponseDto> getMyChallengesListed(String email, ChallengeStatus status) {
        Long memberId = getMemberByEmail(email).getId();
        List<MyChallenge> myChallengesListed;

        switch (status) {
            case PROGRESS:
                    myChallengesListed = myChallengeRepository.findMyChallengesInProgress(memberId, LocalDate.now());
                    break;
            case WAITING:
                    myChallengesListed = myChallengeRepository.findMyChallengesWaiting(memberId, LocalDate.now());
                    break;
            case COMPLETED:
                    myChallengesListed = myChallengeRepository.findMyChallengesCompleted(memberId, LocalDate.now());
                    break;
            default:
                throw new IllegalArgumentException("유효하지 않은 챌린지 상태 입니다.");
        }

        return myChallengesListed.stream()
                .map(myChallenge -> mapToResponseDto(myChallenge, status))
                .collect(Collectors.toList());
    }

    public MyVerifsResponseDto getMyVerifications(Long myChallengeId) {
        MyChallenge myChallenge = getMyChallengeById(myChallengeId);
        return new MyVerifsResponseDto(myChallenge);
    }

    private MyChallengeResponseDto mapToResponseDto(MyChallenge myChallenge, ChallengeStatus status) {
        boolean isGithubConnected = myChallenge.getMember().isGithubConnected();
        boolean isVerified = myChallenge.isVerified();

        switch (status) {
            case PROGRESS:
                return myChallenge.getMyChallengesInProgress(myChallenge, isVerified, isGithubConnected);
            case WAITING:
                return myChallenge.getMyChallengesWaiting(myChallenge);
            case COMPLETED:
                return myChallenge.getMyChallengesCompleted(myChallenge);
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

}
