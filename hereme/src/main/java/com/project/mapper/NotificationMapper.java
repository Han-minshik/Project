<<<<<<< HEAD:hereme/src/main/java/com/project/mapper/NotificationMapper.java
package com.project.mapper;

import com.project.dto.NotificationDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NotificationMapper {
    void createNotification(NotificationDTO notification);
    void updateNotificationStatus(int id, String status);
    List<NotificationDTO> getNotificationByUserId(String userId);
}
=======
package com.project.mapper;

import com.project.dto.NotificationDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NotificationMapper {
    void createNotification(NotificationDTO notification);
    void updateNotificationStatus(Integer id, String status);
    List<NotificationDTO> getNotificationByUserId(String userId);
    void createLoanReminderNotification(@Param("userId") String userId, @Param("message") String message, @Param("type") String type);
    void createPointEarnedNotification(@Param("userId") String userId, @Param("message") String message, @Param("type") String type);
    void createPurchaseNotification(@Param("userId") String userId, @Param("message") String message, @Param("type") String type);
    void createDiscussionNotification(@Param("userId") String userId, @Param("message") String message, @Param("type") String type);
    void createSignUpNotification(@Param("userId") String userId, @Param("message") String message, @Param("type") String type);
}
>>>>>>> 메인-페이지-디자인(정예은):src/main/java/com/project/mapper/NotificationMapper.java
