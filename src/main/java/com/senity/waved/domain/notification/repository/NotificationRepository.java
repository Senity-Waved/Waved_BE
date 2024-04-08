package com.senity.waved.domain.notification.repository;

import com.senity.waved.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByMemberId(Long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM Notification n WHERE n.createDate <= :createdBefore")
    void deleteNotificationsByCreateDate(@Param("createdBefore") ZonedDateTime createdBefore);
}
