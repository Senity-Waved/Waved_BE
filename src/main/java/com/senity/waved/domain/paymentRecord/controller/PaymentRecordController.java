package com.senity.waved.domain.paymentRecord.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments/{myChallengeId}")
public class PaymentRecordController {
//
//    private final PaymentRecordService paymentRecordService;
//
//    @PostMapping
//    public ResponseEntity<String> validatePayment (
//            @AuthenticationPrincipal User user,
//            @PathVariable("myChallengeId") Long myChallengeId,
//            @RequestBody PaymentRequestDto requestDto
//    ) {
//        paymentRecordService.validateAndSavePaymentRecord(user.getUsername(), myChallengeId, requestDto);
//        return new ResponseEntity<>("결제 검증이 완료되었습니다.", HttpStatus.OK);
//    }
//
//    @PostMapping("/cancel")
//    public ResponseEntity<String> cancelChallengePayment (
//            @AuthenticationPrincipal User user,
//            @PathVariable("myChallengeId") Long myChallengeId
//    ) {
//        paymentRecordService.cancelChallengePayment(user.getUsername(), myChallengeId);
//        return new ResponseEntity<>("결제 취소 처리되었습니다.", HttpStatus.OK);
//    }
//
//    @PostMapping("/completed")
//    public ResponseEntity<String> completedChallenge (
//            @AuthenticationPrincipal User user,
//            @PathVariable("myChallengeId") Long myChallengeId
//    ) {
//        String msg = paymentRecordService.checkRefundDepositOrNot(user.getUsername(), myChallengeId);
//        return new ResponseEntity<>(msg, HttpStatus.OK);
//    }
}
