package com.senity.waved.domain.notification.dto.response;

import com.senity.waved.domain.notification.entity.Notification;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;


@Setter
@Getter
@SuperBuilder
public class NotificationResponseDto {
    private String title;
    private String message;
    private ZonedDateTime createDate;

    public static NotificationResponseDto of(Notification notification) {
        return NotificationResponseDto.builder()
                .title(notification.getTitle())
                .message(notification.getMessage())
                .createDate(notification.getCreateDate())
                .build();
    }
}
