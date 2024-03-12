package com.senity.waved.domain.myChallenge.dto.response;

import com.senity.waved.domain.myChallenge.entity.MyChallenge;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyVerifsResponseDto {
    private int[] myVerifs;

    public MyVerifsResponseDto(MyChallenge myChallenge) {
        this.myVerifs = myChallenge.getMyVerifs();
    }
}
