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
