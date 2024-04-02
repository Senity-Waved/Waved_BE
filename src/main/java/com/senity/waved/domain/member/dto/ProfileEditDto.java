package com.senity.waved.domain.member.dto;

import com.senity.waved.domain.member.entity.Gender;
import com.senity.waved.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileEditDto {
    private String nickname;
    private String jobTitle;
    private String birthYear;
    private Gender gender;

    public static ProfileEditDto from(Member member) {
        return ProfileEditDto.builder()
                .nickname(member.getNickname())
                .jobTitle(member.getJobTitle())
                .birthYear(member.getBirthYear())
                .gender(member.getGender())
                .build();
    }
}
