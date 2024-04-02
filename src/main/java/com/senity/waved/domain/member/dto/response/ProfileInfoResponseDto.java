package com.senity.waved.domain.member.dto.response;

import com.senity.waved.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileInfoResponseDto {
    private String nickname;
    private String jobTitle;
    private String githubId;

    public static ProfileInfoResponseDto from(Member member) {
        return ProfileInfoResponseDto.builder()
                .nickname(member.getNickname())
                .jobTitle(member.getJobTitle())
                .githubId(member.getGithubId())
                .build();
    }
}

