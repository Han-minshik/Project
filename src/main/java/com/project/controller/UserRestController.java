package com.project.controller;

import lombok.extern.log4j.Log4j2;
import com.project.mapper.UserMapper;
import com.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/user")
public class UserRestController {
    @Autowired private UserMapper userMapper;
    @Autowired private UserService userService;

    @GetMapping("/id/{userId}")
    public ResponseEntity<Void> find_user_by_id(
            @PathVariable String userId
    ) {
        if(userMapper.getUserById(userId) == null){ // 중복 아니면 200
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FOUND).build(); // 중복이면
    }

    @GetMapping("/name/{userName}")
    public ResponseEntity<Void> find_user_by_nickname(
            @PathVariable String userName
    ) {
        if(userMapper.selectUserByName(userName) == null){ // 중복 아니면 200
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FOUND).build(); // 중복이면
    }

    @GetMapping("/findId/{email}")
    public ResponseEntity<String> get_findId_by_email(@PathVariable String email) {
        String foundId = userMapper.findIdByEmail(email);

        if(foundId != null){
            return ResponseEntity.status(HttpStatus.FOUND).body(foundId);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/changePw/password")
    public ResponseEntity<Void> post_change_password(
        Authentication auth,
        @RequestBody String password
    ){
//        auth.getName();
//        userService.changePassword(password);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
