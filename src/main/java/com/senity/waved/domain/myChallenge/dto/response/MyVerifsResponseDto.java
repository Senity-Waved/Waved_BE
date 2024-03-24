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
        this.myVerifs = myChallenge.getMyVerifs();
        this.groupTitle = myChallenge.getChallengeGroup().getGroupTitle();
        this.startDate = myChallenge.getChallengeGroup().getStartDate();
        this.endDate = myChallenge.getChallengeGroup().getEndDate();
    }
}
