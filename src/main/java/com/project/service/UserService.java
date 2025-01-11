package com.project.service;

import com.project.dto.ReviewDTO;
import com.project.dto.UserDTO;
import com.project.mapper.UserMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Log4j2
@Service
public class UserService {
    @Autowired private UserMapper userMapper;
//    @Autowired private PasswordEncoder passwordEncoder;

    public boolean join_user(UserDTO userDTO) {
        // 유저 중복 방지
        UserDTO findUser = userMapper.getUserById(userDTO.getId());
        if(Objects.nonNull(findUser)) {
            log.error("이미 회원가입이 되어있습니다.");
            return false;
        }
        // 유저 DB에 등록하기 직전에 패스워드를 인코딩해서 삽입
//        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
//        userDTO.setPassword(encodedPassword);
        userMapper.createUser(userDTO);
        log.info("회원가입이 완료되었습니다.");
        return true;
    }
    // 리뷰 작성
    public void write_review(String userId, Integer bookIsbn, ReviewDTO review) {
        review.setUserId(userId);
        review.setBookIsbn(bookIsbn);
        userMapper.insertReview(review);
    }
}
