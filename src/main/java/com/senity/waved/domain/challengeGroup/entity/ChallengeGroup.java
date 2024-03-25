package com.senity.waved.domain.challengeGroup.entity;

import com.senity.waved.common.BaseEntity;
import com.senity.waved.domain.challenge.entity.Challenge;
import com.senity.waved.domain.challengeGroup.dto.response.ChallengeGroupHomeResponseDto;
import com.senity.waved.domain.challengeGroup.dto.response.ChallengeGroupResponseDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;

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

    @Column(name = "challenge_id")
    private Long challengeId;

    public void addGroupParticipantCount() {
        participantCount++;
    }

    public void deleteGroupParticipantCount() {
        if (participantCount < 0) participantCount--;
    }

    public static ChallengeGroupResponseDto getGroupResponse(ChallengeGroup group, Challenge challenge, Long myChallengeId) {
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

    public static ChallengeGroupHomeResponseDto getHomeGroupResponse(ChallengeGroup group, Challenge challenge) {
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
