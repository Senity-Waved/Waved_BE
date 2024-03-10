package com.senity.waved.domain.review.entity;

import com.senity.waved.common.BaseEntity;
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

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "challenge_group_id")
    private Long challengeGroupId;

    public void updateContent(String content) {
        this.content = content;
    }
}
