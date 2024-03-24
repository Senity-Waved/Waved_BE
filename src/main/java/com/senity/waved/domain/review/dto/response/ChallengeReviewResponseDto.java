package com.senity.waved.domain.review.dto.response;

import com.senity.waved.domain.member.entity.Member;
import com.senity.waved.domain.review.entity.Review;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeReviewResponseDto {
    private String nickname;
    private String jobTitle;
    private String content;
    private ZonedDateTime createDate;

    public static ChallengeReviewResponseDto getChallengeReviewResponseDto(Review review, Member member) {
        return ChallengeReviewResponseDto.builder()
                .nickname(member.getNickname())
                .jobTitle(member.getJobTitle())
                .createDate(review.getCreateDate())
                .content(review.getContent())
                .build();
    }
}
