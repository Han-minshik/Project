package com.project.mapper;

import com.project.dto.AdminPostDTO;
import com.project.dto.BookDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminMapper {
    void insertBook(BookDTO book);
    void insertBookImages(BookDTO book);

    void createdAdminPost(AdminPostDTO adminPost);
    AdminPostDTO getAdminPostById(Integer id);
    List<AdminPostDTO> getAllAdminPosts();
    void updateAdminPost(AdminPostDTO adminPost);
    void deleteAdminPost(Integer id);
}
