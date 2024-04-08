package com.senity.waved.domain.notification.controller;

import com.senity.waved.common.ResponseDto;
import com.senity.waved.domain.notification.dto.response.NotificationResponseDto;
import com.senity.waved.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notify")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public List<NotificationResponseDto> getAllNotifications(@AuthenticationPrincipal User user) {
        return notificationService.getNotifications(user.getUsername());
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<ResponseDto> deleteNotification(@PathVariable("notificationId") Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseDto.of(HttpStatus.OK, "알림이 삭제되었습니다.");
    }
}
