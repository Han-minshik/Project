package com.project.mapper;

import com.project.dto.LoanDTO;
import com.project.dto.ReviewDTO;
import com.project.dto.SnsInfoDTO;
import com.project.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    void createUser(UserDTO user);
    UserDTO getUserById(String id);
    UserDTO getUserByCi(String ci);
    List<UserDTO> getAllUsers();
    void updateUser(UserDTO userDTO);
    void deleteUser(String id);
    List<ReviewDTO> getReviewsByUserId(String userId);
    void insertSnsInfo(SnsInfoDTO snsInfo);
    void insertReview(ReviewDTO review);
    void addPointToUser(@Param("userId") String userId, @Param("points") Integer points);
    String getTopDiscussionUser();
    String getTopCommentUserByDiscussionId(@Param("discussionId") Integer discussionId);
    void addPointGrantHistory(@Param("userId") String userId, @Param("points") Integer points, @Param("reason") String reason);
    boolean hasPointGrantedForReason(@Param("userId") String userId, @Param("reason") String reason);
    Integer deductPoints(@Param("userId") String userId, @Param("points") Integer points);
}
