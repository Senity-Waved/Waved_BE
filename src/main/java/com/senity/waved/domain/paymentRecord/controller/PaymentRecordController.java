package com.senity.waved.domain.paymentRecord.controller;

import com.senity.waved.domain.paymentRecord.dto.request.PaymentRequestDto;
import com.senity.waved.domain.paymentRecord.service.PaymentRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments/{myChallengeId}")
public class PaymentRecordController {

    private final PaymentRecordService paymentRecordService;

    @PostMapping
    public ResponseEntity<String> validatePayment (
            @AuthenticationPrincipal User user,
            @PathVariable("myChallengeId") Long myChallengeId,
            @RequestBody PaymentRequestDto requestDto
    ) {
        paymentRecordService.validateAndSavePaymentRecord(user.getUsername(), myChallengeId, requestDto);
        return new ResponseEntity<>("결제 검증이 완료되었습니다.", HttpStatus.OK);
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelChallengePayment (
            @AuthenticationPrincipal User user,
            @PathVariable("myChallengeId") Long myChallengeId
    ) {
        paymentRecordService.cancelChallengePayment(user.getUsername(), myChallengeId);
        return new ResponseEntity<>("결제 취소 처리되었습니다.", HttpStatus.OK);
    }

    @PostMapping("/completed")
    public ResponseEntity<String> completedChallenge (
            @AuthenticationPrincipal User user,
            @PathVariable("myChallengeId") Long myChallengeId
    ) {
        String msg = paymentRecordService.checkRefundDepositOrNot(user.getUsername(), myChallengeId);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
}
