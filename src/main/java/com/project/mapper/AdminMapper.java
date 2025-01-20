package com.project.mapper;

import com.project.dto.AdminPostDTO;
import com.project.dto.BookDTO;
import com.project.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminMapper {
    void createAdmin(String userId);
    void insertBook(BookDTO book);
    void updateBook(BookDTO book);
    void deleteBook(String isbn); // Integer → String
    void createAdminPost(AdminPostDTO adminPost);
    List<AdminPostDTO> getAdminPostById(Integer id);
    List<AdminPostDTO> getAllAdminPosts();
    void updateAdminPost(AdminPostDTO adminPost);
    void deleteAdminPost(Integer id);
    List<UserDTO> getUpdatedUser();
}
