package com.senity.waved.domain.myChallenge.dto.response;

import com.senity.waved.domain.myChallenge.entity.MyChallenge;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyChallengeResponseDto {

    private int[] myVerifs;

    public MyChallengeResponseDto(MyChallenge myChallenge) {
        this.myVerifs = myChallenge.getMyVerifs();
    }
}
