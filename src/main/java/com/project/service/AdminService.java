package com.project.service;

import com.project.dto.AdminPostDTO;
import com.project.dto.BookDTO;
import com.project.dto.UserDTO;
import com.project.mapper.AdminMapper;
import com.project.mapper.UserMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class AdminService {
    @Autowired private AdminMapper adminMapper;
    @Autowired private UserMapper userMapper;

    public void addBook(BookDTO book) {
        adminMapper.insertBook(book);
        log.info("책을 DB에 삽입했습니다 : {}", book.getTitle());

        adminMapper.insertBookImages(book);
        log.info("책의 이미지를 DB에 삽입했습니다. : {}", book.getTitle());
    }

//    public void addAdminPost(AdminPostDTO adminPost) {
//        adminMapper.createAdminPost(adminPost);
//        log.info("공지사항을 DB에 삽입했습니다 : {}", adminPost.getTitle());
//    }
}
