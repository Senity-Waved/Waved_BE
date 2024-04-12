package com.senity.waved.domain.challengeGroup.entity;

import com.senity.waved.common.BaseEntity;
import com.senity.waved.domain.challenge.entity.Challenge;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Duration;
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

    public void updateGroupParticipantCount(Long change) {
        if (participantCount + change >= 0) {
            participantCount += change;
        }
    }

    public static ChallengeGroup from(ChallengeGroup latest, Challenge challenge) {
        Duration term = Duration.between(latest.getStartDate(), latest.getEndDate());
        ZonedDateTime newStartDate = latest.getStartDate().plus(term).plusDays(1);
        ZonedDateTime newEndDate = latest.getEndDate().plus(term).plusDays(1);
        Long newGroupIndex = latest.getGroupIndex() + 1L;
        String newGroupTitle = String.format("%s %d기", challenge.getTitle(), newGroupIndex);

        return ChallengeGroup.builder()
                .groupIndex(newGroupIndex)
                .groupTitle(newGroupTitle)
                .startDate(newStartDate)
                .endDate(newEndDate)
                .challengeId(latest.getChallengeId())
                .participantCount(0L)
                .build();
    }
}
