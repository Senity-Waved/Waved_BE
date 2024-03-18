package com.senity.waved.domain.review.entity;

import com.senity.waved.common.BaseEntity;
import com.senity.waved.domain.challengeGroup.entity.ChallengeGroup;
import com.senity.waved.domain.member.entity.Member;
import com.senity.waved.domain.review.dto.response.ReviewResponseDto;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_group_id")
    private ChallengeGroup challengeGroup;

    @Column(name = "challenge_id")
    private Long challengeId;

    public void updateContent(String content) {
        this.content = content;
    }

    public static ReviewResponseDto getMemberReviewResponse(Review review) {
        return ReviewResponseDto.builder()
                .groupTitle(review.getChallengeGroup().getGroupTitle())
                .createDate(review.getCreateDate())
                .challengeId(review.challengeId)
                .content(review.getContent())
                .build();
    }

    public static ReviewResponseDto getChallengeReviewResponse(Review review) {
        Member member = review.getMember();
        return ReviewResponseDto.builder()
                .nickname(member.getNickname())
                .jobTitle(member.getJobTitle())
                .createDate(review.getCreateDate())
                .content(review.getContent())
                .build();
    }
}
