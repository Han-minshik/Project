package com.project.mapper;

import com.project.dto.AdminPostDTO;
import com.project.dto.BookDTO;
import com.project.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminMapper {
    void createAdmin(UserDTO admin); // 보류
    void insertBook(BookDTO book); // 완료
    void insertBookImages(BookDTO book); // 완료

    //    void updateBook(BookDTO book);
//    void deleteBook(Integer isbn);




    void createAdminPost(AdminPostDTO adminPost); // 완료
    AdminPostDTO getAdminPostById(Integer id); // 완료
    List<AdminPostDTO> getAllAdminPosts(); // 완료
    void updateAdminPost(AdminPostDTO adminPost); // 완료
    void deleteAdminPost(Integer id); // 완료
}
