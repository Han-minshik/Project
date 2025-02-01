package com.project.mapper;

import com.project.dto.AdminPostDTO;
import com.project.dto.BookDTO;
import com.project.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminMapper {
    void createAdmin(@Param("userId") String userId);
    void insertBook(@Param("book") BookDTO book);
    void updateBook(@Param("book") BookDTO book);
    void deleteBook(@Param("isbn")  String isbn); // Integer → String
    void createAdminPost(@Param("adminPost") AdminPostDTO adminPost);
    List<AdminPostDTO> getAdminPostById(@Param("id") Integer id);
    List<AdminPostDTO> getAllAdminPosts();
    void updateAdminPost(@Param("adminPost")AdminPostDTO adminPost);
    void deleteAdminPost(@Param("id") Integer id);
    List<UserDTO> getUpdatedUser();
    List<UserDTO> getAllUser();
    void deleteUser(@Param("id") String userId);
    void answerToUser(@Param("complainNo") Integer complainNo, @Param("answer") String answer);
    List<UserDTO> getPublicUser();
}
