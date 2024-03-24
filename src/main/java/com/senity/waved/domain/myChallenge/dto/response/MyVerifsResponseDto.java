package com.senity.waved.domain.myChallenge.dto.response;

import com.senity.waved.domain.myChallenge.entity.MyChallenge;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class MyVerifsResponseDto {
    private int[] myVerifs;
    private String groupTitle;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;

    public MyVerifsResponseDto(MyChallenge myChallenge) {
        myVerifs = new int[14];
        long longMyVerifs = myChallenge.getMyVerifs();

        for (int i = 1; i < 15; i++) {
            myVerifs[i-1]  = (int) (longMyVerifs / Math.pow(10, 14 - i) % 10);
        }
        this.groupTitle = myChallenge.getChallengeGroup().getGroupTitle();
        this.startDate = myChallenge.getChallengeGroup().getStartDate();
        this.endDate = myChallenge.getChallengeGroup().getEndDate();
    }
}
