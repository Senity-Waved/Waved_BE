package com.senity.waved.domain.paymentRecord.controller;

import com.senity.waved.common.ResponseDto;
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
    public ResponseEntity<ResponseDto> validatePayment (
            @AuthenticationPrincipal User user,
            @PathVariable("myChallengeId") Long myChallengeId,
            @RequestBody PaymentRequestDto requestDto
    ) {
        paymentRecordService.validateAndSavePaymentRecord(user.getUsername(), myChallengeId, requestDto);
        return ResponseDto.of(HttpStatus.OK, "결제 검증이 완료되었습니다.");
    }

    @PostMapping("/cancel")
    public ResponseEntity<ResponseDto> cancelChallengePayment (
            @AuthenticationPrincipal User user,
            @PathVariable("myChallengeId") Long myChallengeId
    ) {
        paymentRecordService.cancelChallengePayment(user.getUsername(), myChallengeId);
        return ResponseDto.of(HttpStatus.OK, "결제 취소 처리되었습니다.");
    }

    @PostMapping("/completed")
    public ResponseEntity<ResponseDto> completedChallenge (
            @AuthenticationPrincipal User user,
            @PathVariable("myChallengeId") Long myChallengeId
    ) {
        String msg = paymentRecordService.checkDepositRefundedOrNot(user.getUsername(), myChallengeId);
        return ResponseDto.of(HttpStatus.OK, msg);
    }
}
