<<<<<<< HEAD:hereme/src/main/java/com/project/mapper/AdminMapper.java
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
=======
package com.project.mapper;

import com.project.dto.AdminPostDTO;
import com.project.dto.BookDTO;
import com.project.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminMapper {
    void createAdmin(UserDTO admin);
    void insertBook(BookDTO book);
    void insertBookImages(BookDTO book);

    void createAdminPost(AdminPostDTO adminPost);
    AdminPostDTO getAdminPostById(Integer id);
    List<AdminPostDTO> getAllAdminPosts();
    void updateAdminPost(AdminPostDTO adminPost);
    void deleteAdminPost(Integer id);
}
>>>>>>> 메인-페이지-디자인(정예은):src/main/java/com/project/mapper/AdminMapper.java
