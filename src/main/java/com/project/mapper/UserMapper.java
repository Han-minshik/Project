package com.project.mapper;

import com.project.dto.ReviewDTO;
import com.project.dto.SnsInfoDTO;
import com.project.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    void createUser(UserDTO user);
    UserDTO getUserById(String id);
    List<UserDTO> getAllUsers();
    void updateUser(UserDTO userDTO);
    void deleteUser(String id);
    List<ReviewDTO> getReviewsByUserId(String userId);

    String findIdByEmail(String email);

    UserDTO getUserByCi(String ci);

    void createSnsInfo(SnsInfoDTO snsInfoDTO);
}
