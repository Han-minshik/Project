package com.project.service;

import com.project.dto.AdminPostDTO;
import com.project.dto.BookDTO;
import com.project.dto.UserDTO;
import com.project.mapper.AdminMapper;
import com.project.mapper.BookMapper;
import com.project.mapper.UserMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class AdminService {
    @Autowired private AdminMapper adminMapper;
    @Autowired private UserMapper userMapper;

    private boolean isAdmin(String userId) {
        UserDTO user = userMapper.getUserById(userId);
        return user != null && "관리자".equals(user.getRole());
    }

    public void updateBook(BookDTO book) {
        adminMapper.updateBook(book);
    }

    public void addBook(String adminId, BookDTO book) {
        if(!isAdmin(adminId)) {
            throw new SecurityException("관리자 권한이 없습니다.");
        }

        adminMapper.insertBook(book);
        log.info("책을 DB에 삽입했습니다 : {}", book.getTitle());

        adminMapper.insertBookImages(book);
        log.info("책의 이미지를 DB에 삽입했습니다. : {}", book.getTitle());
    }

    public void addAdminPost(String adminId, AdminPostDTO adminPost) {
        if(!isAdmin(adminId)) {
            throw new SecurityException("관리자 권한이 없습니다.");
        }
        adminMapper.createAdminPost(adminPost);
        log.info("공지사항을 DB에 삽입했습니다 : {}", adminPost.getTitle());
    }
}
