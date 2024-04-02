package com.senity.waved.domain.review.dto.response;

import com.senity.waved.domain.review.entity.Review;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberReviewResponseDto {
    private Long reviewId;
    private String groupTitle;
    private String content;
    private ZonedDateTime createDate;
    private Long challengeId;

    public static MemberReviewResponseDto from(Review review) {
        return MemberReviewResponseDto.builder()
                .reviewId(review.getId())
                .groupTitle(review.getGroupTitle())
                .createDate(review.getCreateDate())
                .content(review.getContent())
                .challengeId(review.getChallengeId())
                .build();
    }
}
