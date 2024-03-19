package com.senity.waved.domain.review.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewResponseDto {
    private String nickname;
    private String jobTitle;
    private String groupTitle;
    private String content;
    private ZonedDateTime createDate;
    private Long challengeId;
}
