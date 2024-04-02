package com.senity.waved.domain.member.dto;

import com.senity.waved.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GithubInfoDto {
    private String githubId;
    private String githubToken;

    public static GithubInfoDto from(Member member) {
        return GithubInfoDto.builder()
                .githubId(member.getGithubId())
                .githubToken(member.getGithubToken())
                .build();
    }

    public static GithubInfoDto deleteGithubInfo() {
        return GithubInfoDto.builder()
                .githubId(null)
                .githubToken(null)
                .build();
    }
}
