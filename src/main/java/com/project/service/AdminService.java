package com.project.service;

import com.project.dto.BookDTO;
import com.project.mapper.AdminMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class AdminService {
    @Autowired private AdminMapper adminMapper;
    public void add_book(BookDTO book) {
        adminMapper.insertBook(book);
        log.info("책을 DB에 삽입함.");

        adminMapper.insertBookImages(book);
        log.info("책의 이미지(를)을 DB에 삽입함.");
    }
}
