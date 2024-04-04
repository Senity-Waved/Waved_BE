package com.senity.waved.domain.notification.entity;

import com.senity.waved.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Notification extends BaseEntity {

    @Column(name = "title")
    private String title;

    @Column(name = "message")
    private String message;

    @Column(name = "member_id")
    private Long memberId;

    public static Notification of(Long memberId, String title, String message) {
        return Notification.builder()
                .memberId(memberId)
                .title(title)
                .message(message)
                .build();
    }
}
