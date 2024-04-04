package com.senity.waved.domain.notification.service;

import com.senity.waved.domain.notification.dto.response.NotificationResponseDto;

import java.util.List;

public interface NotificationService {

    List<NotificationResponseDto> getNotifications(String email);
}
