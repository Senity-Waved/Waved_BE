package com.senity.waved.domain.liked.controller;

import com.senity.waved.common.ResponseDto;
import com.senity.waved.domain.liked.dto.response.LikedResponseDto;
import com.senity.waved.domain.liked.service.LikedService;
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
@RequestMapping("/api/v1/likes/{verificationId}")
public class LikedController {

    private final LikedService likedService;

    @PostMapping
    public ResponseEntity<ResponseDto> addLikedToVerification(
            @PathVariable("verificationId") Long verificationId,
            @AuthenticationPrincipal User user
    ) {
        likedService.addLikedToVerification(user.getUsername(), verificationId);
        return ResponseDto.of(HttpStatus.OK, "좋아요를 추가했습니다.");
    }

    @GetMapping
    public LikedResponseDto getCountLikesByVerification(
            @PathVariable("verificationId") Long verificationId
    ) {
        Long likedCount = likedService.countLikesToVerification(verificationId);
        return new LikedResponseDto(verificationId, likedCount);
    }

    @DeleteMapping
    public ResponseEntity<ResponseDto> removeLikeFromVerification(
            @PathVariable("verificationId") Long verificationId,
            @AuthenticationPrincipal User user
    ) {
        likedService.removeLikeFromVerification(user.getUsername(), verificationId);
        return ResponseDto.of(HttpStatus.OK, "좋아요를 취소했습니다.");
    }

}
