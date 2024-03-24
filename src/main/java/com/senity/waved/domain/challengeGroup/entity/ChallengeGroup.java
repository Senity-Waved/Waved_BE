package com.senity.waved.domain.challengeGroup.entity;

import com.senity.waved.common.BaseEntity;
import com.senity.waved.domain.challenge.entity.Challenge;
import com.senity.waved.domain.challengeGroup.dto.response.ChallengeGroupHomeResponseDto;
import com.senity.waved.domain.challengeGroup.dto.response.ChallengeGroupResponseDto;
import com.senity.waved.domain.myChallenge.entity.MyChallenge;
import com.senity.waved.domain.quiz.entity.Quiz;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class ChallengeGroup extends BaseEntity {

    @Column(name = "group_index")
    private Long groupIndex;

    @Column(name = "title")
    private String groupTitle;

    @Column(name = "participant_count")
    private Long participantCount;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    private ZonedDateTime endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<MyChallenge> myChallenges = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Quiz> quizzes = new ArrayList<>();

    public void addMyChallenge(MyChallenge myChallenge) {
        myChallenges.add(myChallenge);
        participantCount++;
    }

    public void deleteMyChallenge(MyChallenge myChallenge) {
        myChallenges.remove(myChallenge);
        participantCount--;
    }

    public static ChallengeGroupResponseDto getGroupResponse(ChallengeGroup group, Long myChallengeId) {
        Challenge challenge = group.getChallenge();
        Boolean isApplied = myChallengeId > 0 ? true : false;
        return ChallengeGroupResponseDto.builder()
                .groupTitle(group.getGroupTitle())
                .participantCount(group.getParticipantCount())
                .startDate(group.getStartDate())
                .endDate(group.getEndDate())
                .verificationType(challenge.getVerificationType())
                .description(challenge.getDescription())
                .verificationDescription(challenge.getVerificationDescription())
                .isApplied(isApplied)
                .isFree(challenge.getIsFree())
                .myChallengeId(myChallengeId)
                .challengeId(challenge.getId())
                .build();
    }

    public static ChallengeGroupResponseDto getGroupAdminResponse(ChallengeGroup group) {
        Challenge challenge = group.getChallenge();
        return ChallengeGroupResponseDto.builder()
                .groupTitle(group.getGroupTitle())
                .participantCount(group.getParticipantCount())
                .startDate(group.getStartDate())
                .endDate(group.getEndDate())
                .verificationType(challenge.getVerificationType())
                .challengeId(challenge.getId())
                .build();
    }

    public static ChallengeGroupHomeResponseDto getHomeGroupResponse(ChallengeGroup group) {
        Challenge challenge = group.getChallenge();
        return ChallengeGroupHomeResponseDto.builder()
                .groupTitle(group.getGroupTitle())
                .verificationType(challenge.getVerificationType())
                .isFree(challenge.getIsFree())
                .participantCount(group.getParticipantCount())
                .startDate(group.getStartDate())
                .challengeGroupId(group.getId())
                .build();
    }
}
