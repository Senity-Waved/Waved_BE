package com.senity.waved.domain.review.entity;

import com.senity.waved.common.BaseEntity;
import com.senity.waved.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Review extends BaseEntity {

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "group_title")
    private String groupTitle;

/*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_group_id")
    private ChallengeGroup challengeGroup;
*/

    @Column(name = "challenge_id")
    private Long challengeId;

    public void updateContent(String content) {
        this.content = content;
    }
}
