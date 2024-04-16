package com.senity.waved.domain.paymentRecord.service;

import com.senity.waved.domain.challengeGroup.entity.ChallengeGroup;
import com.senity.waved.domain.challengeGroup.repository.ChallengeGroupRepository;
import com.senity.waved.domain.member.entity.Member;
import com.senity.waved.domain.member.exception.MemberNotFoundException;
import com.senity.waved.domain.member.repository.MemberRepository;
import com.senity.waved.domain.myChallenge.entity.MyChallenge;
import com.senity.waved.domain.myChallenge.exception.MyChallengeNotFoundException;
import com.senity.waved.domain.myChallenge.repository.MyChallengeRepository;
import com.senity.waved.domain.paymentRecord.dto.request.PaymentRequestDto;
import com.senity.waved.domain.paymentRecord.entity.PaymentRecord;
import com.senity.waved.domain.paymentRecord.entity.PaymentStatus;
import com.senity.waved.domain.paymentRecord.exception.DepositAmountNotMatchException;
import com.senity.waved.domain.paymentRecord.exception.MemberAndMyChallengeNotMatchException;
import com.senity.waved.domain.paymentRecord.repository.PaymentRecordRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.temporal.ChronoUnit;


@Slf4j
@Service
@AllArgsConstructor
public class PaymentRecordServiceImpl implements PaymentRecordService {

    private final MemberRepository memberRepository;
    private final MyChallengeRepository myChallengeRepository;
    private final PaymentRecordRepository paymentRecordRepository;
    private final ChallengeGroupRepository challengeGroupRepository;
    private IamportClient api;

    @Override
    @Transactional
    public void validateAndSavePaymentRecord(String email, Long myChallengeId, PaymentRequestDto requestDto) {
        Member member = getMemberByEmail(email);
        MyChallenge myChallenge = getMyChallengeById(myChallengeId);
        validateMember(member, myChallenge);

        if (!myChallenge.getDeposit().equals(requestDto.getDeposit())) {
            cancelChallengePayment(email, myChallengeId);
            myChallengeRepository.deleteById(myChallengeId);
            throw new DepositAmountNotMatchException("마이 챌린지의 예치금과 결제 금액이 일치하지 않습니다.");
        }
        savePaymentRecord(myChallenge, member.getId(), PaymentStatus.APPLIED);
        myChallenge.updateImpUid(requestDto.getImp_uid());
        myChallenge.updateIsPaid(true);
        myChallengeRepository.save(myChallenge);
    }

    @Override
    @Transactional
    public void cancelChallengePayment(String email, Long myChallengeId) {
        Member member = getMemberByEmail(email);
        MyChallenge myChallenge = getMyChallengeById(myChallengeId);

        validateMember(member, myChallenge);
        cancelImportPayment(String.valueOf(myChallenge.getImpUid()));

        savePaymentRecord(myChallenge, member.getId(), PaymentStatus.CANCELED);
        myChallengeRepository.delete(myChallenge);
    }

    @Override
    @Transactional
    public String checkDepositRefundedOrNot(String email, Long myChallengeId) {
        Member member = getMemberByEmail(email);
        MyChallenge myChallenge = getMyChallengeById(myChallengeId);
        ChallengeGroup group = getGroupById(myChallenge.getChallengeGroupId());

        long days = ChronoUnit.DAYS.between(group.getStartDate(), group.getEndDate());
        int successCount = days > 10 ? 10 : 5;

        PaymentStatus status = myChallenge.getSuccessCount() < successCount ?
                PaymentStatus.FAIL : PaymentStatus.SUCCESS;

        String message = myChallenge.getSuccessCount() < successCount ?
                "챌린지 성공률을 달성하지 못해 예치금을 환급받지 못했습니다." : "챌린지 성공률을 달성해 예치금을 환급받았습니다.";

        if (status == PaymentStatus.SUCCESS && myChallenge.getDeposit() != 0) {
            cancelImportPayment(String.valueOf(myChallenge.getImpUid()));
        }

        myChallenge.updateIsRefundRequested();
        savePaymentRecord(myChallenge, member.getId(), status);
        return message;
    }

    public void savePaymentRecord(MyChallenge myChallenge, Long memberId, PaymentStatus status) {
        ChallengeGroup group = getGroupById(myChallenge.getChallengeGroupId());
        String groupTitle = group.getGroupTitle();
        updateGroupParticipantCount(group, status);

        try {
            PaymentRecord paymentRecord = PaymentRecord.of(status, memberId, myChallenge, groupTitle);
            // paymentRecordRepository.save(paymentRecord);
        } catch (Exception e) {
            // throw new PaymentRecordExistException("이미 존재하는 예치금 내역입니다.");
            log.error("------------------- sql exception");
        }
    }

    private void cancelImportPayment(String impUid) {
        try {
            CancelData cancelData = new CancelData(impUid, true);
            api.cancelPaymentByImpUid(cancelData);
        } catch (IamportResponseException | IOException e) {
            throw new RuntimeException("결제 취소 중 오류가 발생했습니다.", e);
        }
    }

    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다."));
    }

    private MyChallenge getMyChallengeById(Long id) {
        return myChallengeRepository.findById(id)
                .orElseThrow(() -> new MyChallengeNotFoundException("해당 마이 챌린지를 찾을 수 없습니다."));
    }

    private ChallengeGroup getGroupById(Long id) {
        return challengeGroupRepository.findById(id)
                .orElseThrow(() -> new MyChallengeNotFoundException("해당 마이 챌린지를 찾을 수 없습니다."));
    }

    private void validateMember(Member member, MyChallenge myChallenge) {
        if(!myChallenge.getMemberId().equals(member.getId()))
            throw new MemberAndMyChallengeNotMatchException("해당 멤버의 마이 챌린지가 아닙니다.");
    }

    private void updateGroupParticipantCount(ChallengeGroup group, PaymentStatus status) {
        if (status.equals(PaymentStatus.APPLIED)) {
            group.addParticipantCount();
        } else if (status.equals(PaymentStatus.CANCELED)) {
            group.subtractParticipantCount();
        }
    }
}
