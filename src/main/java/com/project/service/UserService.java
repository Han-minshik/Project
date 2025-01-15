package com.project.service;

import com.project.dto.UserDTO;
import com.project.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired private UserMapper userMapper;
    @Autowired private PasswordEncoder passwordEncoder;

    public boolean sign_up_user(UserDTO user) {
        // 만약에 유저id가 유니크라면?

        UserDTO findUser = userMapper.getUserById(user.getId());
        if (findUser != null) { // 유저 중복이면?
            return false;
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userMapper.createUser(user);
        return true;
    }

    public boolean change_password(String id, String oldPw, String newPw) {
        if(!oldPw.equals(newPw)) {
            UserDTO findUser = userMapper.getUserById(id);
            findUser.setPassword(newPw);
            userMapper.updateUser(findUser);
            return true;
        }
        return false;
    }

}
