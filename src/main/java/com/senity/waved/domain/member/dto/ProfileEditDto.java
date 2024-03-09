package com.senity.waved.domain.member.dto;

import com.senity.waved.domain.member.entity.Gender;
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
}
