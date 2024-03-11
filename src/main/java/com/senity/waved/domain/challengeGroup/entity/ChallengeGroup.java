package com.senity.waved.domain.challengeGroup.entity;

import com.senity.waved.common.BaseEntity;

import com.senity.waved.domain.challenge.entity.Challenge;
import com.senity.waved.domain.challengeGroup.dto.response.ChallengeGroupResponseDto;
import com.senity.waved.domain.myChallenge.entity.MyChallenge;
import com.senity.waved.domain.quiz.entity.Quiz;
import com.senity.waved.domain.verification.entity.Verification;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Date;
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

    @Temporal(TemporalType.DATE)
    @Column(name = "start_date")
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_date")
    private Date endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @OneToMany(mappedBy = "challengeGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MyChallenge> myChallenges = new ArrayList<>();

    @OneToMany(mappedBy = "challengeGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Verification> verifications  = new ArrayList<>();

    @OneToMany(mappedBy = "challengeGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Quiz> quizzes = new ArrayList<>();

    public static ChallengeGroupResponseDto getGroupResponse(ChallengeGroup group) {
        return ChallengeGroupResponseDto.builder()
                .groupTitle(group.getGroupTitle())
                .participantCount(group.getParticipantCount())
                .startDate(group.getStartDate())
                .endDate(group.getEndDate())
                .verificationType(group.getChallenge().getVerificationType())
                .description(group.getChallenge().getDescription())
                .verificationDescription(group.getChallenge().getVerificationDescription())
                .challengeId(group.getChallenge().getId())
                .build();
    }
}
