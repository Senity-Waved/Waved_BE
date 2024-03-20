package com.senity.waved.domain.paymentRecord.service;

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
import com.senity.waved.domain.paymentRecord.exception.MemberAndMyChallengeNotMatch;
import com.senity.waved.domain.paymentRecord.repository.PaymentRecordRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class PaymentRecordServiceImpl implements PaymentRecordService {

    private final MemberRepository memberRepository;
    private final MyChallengeRepository myChallengeRepository;
    private final PaymentRecordRepository paymentRecordRepository;
    private final IamportClient api;
    
    @Override
    @Transactional
    public void validateAndSavePaymentRecord(String email, Long myChallengeId, PaymentRequestDto requestDto) {
        Member member = getMemberByEmail(email);
        MyChallenge myChallenge = getMyChallenge(myChallengeId);
        validateMember(member, myChallenge);

        if (!myChallenge.getDeposit().equals(requestDto.getDeposit())) {
            cancelChallengePayment(email, myChallengeId);
            myChallengeRepository.deleteById(myChallengeId);

            throw new DepositAmountNotMatchException("마이 챌린지의 예치금과 결제 금액이 일치하지 않습니다.");
        }

        PaymentRecord paymentRecord = PaymentRecord.of(PaymentStatus.APPLIED, myChallenge.getDeposit(), member, myChallengeId);
        paymentRecordRepository.save(paymentRecord);
    }

    @Override
    @Transactional
    public void cancelChallengePayment(String email, Long myChallengeId) {
        Member member = getMemberByEmail(email);
        MyChallenge myChallenge = getMyChallenge(myChallengeId);
        validateMember(member, myChallenge);

        try {
            IamportResponse<Payment> iamportResponse = api.paymentByImpUid(String.valueOf(myChallenge.getImpUid()));
            BigDecimal paidAmount = iamportResponse.getResponse().getAmount(); // 사용자가 실제 결제한 금액

            CancelData cancelData = new CancelData(String.valueOf(myChallenge.getImpUid()), true);;
            api.cancelPaymentByImpUid(cancelData);

            PaymentRecord paymentRecord = PaymentRecord.of(PaymentStatus.CANCELED, myChallenge.getDeposit(), member, myChallengeId);
            paymentRecordRepository.save(paymentRecord);
        }
        catch (IamportResponseException | IOException e) {
            throw new RuntimeException("결제 취소 중 오류가 발생했습니다.");

        }

        myChallenge.markAsDeleted(true);
        myChallengeRepository.save(myChallenge);
    }

    @Override
    @Transactional
    public String checkRefundDepositOrNot(String email, Long myChallengeId) {
        Member member = getMemberByEmail(email);
        MyChallenge myChallenge = getMyChallenge(myChallengeId);
        PaymentStatus status;
        StringBuilder msg = new StringBuilder();

        if (myChallenge.getSuccessCount() < 11) {
            status = PaymentStatus.FAIL;
            msg.append("챌린지 성공률을 달성하지 못해 예치금을 환급받지 못했습니다.");
        } else {
            status = PaymentStatus.SUCCESS;
            cancelChallengePayment(email, myChallengeId);
            msg.append("챌린지 성공률을 달성해 예치금을 환급받았습니다.");
        }

        PaymentRecord paymentRecord = PaymentRecord.of(status, myChallenge.getDeposit(), member, myChallengeId);
        paymentRecordRepository.save(paymentRecord);
        return String.valueOf(msg);
    }

    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다."));
    }

    private MyChallenge getMyChallenge(Long myChallengeId) {
        return myChallengeRepository.findById(myChallengeId)
                .orElseThrow(() -> new MyChallengeNotFoundException("해당 마이 챌린지를 찾을 수 없습니다."));
    }

    private void validateMember(Member member, MyChallenge myChallenge) {
        if(!myChallenge.getMember().equals(member))
            throw new MemberAndMyChallengeNotMatch("해당 멤버의 마이 챌린지가 아닙니다.");
    }
}
