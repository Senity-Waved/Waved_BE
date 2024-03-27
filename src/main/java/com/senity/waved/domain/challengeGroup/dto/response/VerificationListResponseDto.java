package com.senity.waved.domain.challengeGroup.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.senity.waved.domain.member.entity.Member;
import com.senity.waved.domain.verification.entity.Verification;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
@Setter
public class VerificationListResponseDto {
    private Long verificationId;
    private String content;
    private String link;
    private String imageUrl;
    private ZonedDateTime verificationDate;
    private Long likesCount;
    private String nickname;
    private Long memberId;

    @JsonProperty("isLiked")
    private boolean isLiked;

    public VerificationListResponseDto(Verification verification, Member member, boolean isLiked) {
        this.verificationId = verification.getId();
        this.content = verification.getContent();
        this.link = verification.getLink();
        this.imageUrl = verification.getImageUrl();
        this.isLiked = isLiked;
        this.likesCount = verification.getLikesCount();
        this.nickname = member.getNickname();
        this.memberId = member.getId();

        if (verification.getCreateDate() != null) {
            LocalDateTime localDateTime = verification.getCreateDate().toLocalDateTime().plusHours(9);
            this.verificationDate = ZonedDateTime.of(localDateTime, ZoneId.of("Asia/Seoul"));
        }
    }
}