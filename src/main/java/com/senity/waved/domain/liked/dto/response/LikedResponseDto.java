package com.senity.waved.domain.liked.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikedResponseDto {
    private Long verificationId;
    private Long likedCount;
}
