package com.project.mapper;

import com.project.dto.AdminPostDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminMapper {
    void createdAdminPost(AdminPostDTO adminPost);
    AdminPostDTO getAdminPostById(int id);
    List<AdminPostDTO> getAllAdminPosts();
    void updateAdminPost(AdminPostDTO adminPost);
    void deleteAdminPost(int id);
}
