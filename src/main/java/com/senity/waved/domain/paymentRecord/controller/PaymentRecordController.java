package com.senity.waved.domain.paymentRecord.controller;

import com.senity.waved.domain.paymentRecord.service.PaymentRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments/{myChallengeId}")
public class PaymentRecordController {

    private final PaymentRecordController paymentRecordController;

    @PostMapping
    public void validatePayment (
            @AuthenticationPrincipal User user,
            @PathVariable("myChallengeId") Long myChallengeId

    ) {
        return ;
    }

    @DeleteMapping
    public void cancelChallenge (
            @AuthenticationPrincipal User user,
            @PathVariable("myChallengeId") Long myChallengeId
    ) {
        return;
    }

    @PostMapping("/completed")
    public void completedChallenge (
            @AuthenticationPrincipal User user,
            @PathVariable("myChallengeId") Long myChallengeId
    ) {
        return PaymentRecordService.checkRefundDepositOrNot(user.getUsername(), myChallengeId);
    }
}
