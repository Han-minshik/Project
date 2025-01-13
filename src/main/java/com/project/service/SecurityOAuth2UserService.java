package com.project.service;

import lombok.extern.log4j.Log4j2;
import com.project.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class SecurityOAuth2UserService extends DefaultOAuth2UserService {
    @Autowired private UserMapper userMapper;

    private final String CI = "";

//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        OAuth2User oAuth2User = super.loadUser(userRequest);
//
//    }

//    UserDTO existedUser = userMapper.selectUserByCi
}
