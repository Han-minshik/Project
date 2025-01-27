package com.project.mapper;

import com.project.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    void createUser(@Param("user") UserDTO user);
    UserDTO getUserById(@Param("id") String id);
    UserDTO getUserByCi(@Param("ci") String ci);
    List<UserDTO> getAllUsers();
    void updateUser(@Param("userDTO") UserDTO userDTO);
    void deleteUser(@Param("id") String id);
    List<ReviewDTO> getReviewsByUserId(@Param("userId") String userId);
    void insertSnsInfo(@Param("snsInfo") SnsInfoDTO snsInfo);
    void insertReview(@Param("review") ReviewDTO review);
    void addPointToUser(@Param("userId") String userId, @Param("points") Integer points);
    String getTopDiscussionUser();
    String getTopCommentUserByDiscussionId(@Param("discussionId") Integer discussionId);
    Integer deductPoints(@Param("userId") String userId, @Param("points") Integer points);
    Integer getCommentCountByUserInDiscussion(@Param("userId") String userId, @Param("discussionId") Integer discussionId);
    List<Integer> getAllDiscussionsByUser(@Param("userId") String userId);
    void insertComplain(@Param("userId") ComplainDTO userId);
    void deleteComplain(@Param("complainNo") Integer complainNo);
    String findIdByEmail(@Param("email") String email);
    List<ComplainDTO> getComplains();
    List<ComplainDTO> getMyComplains(@Param("userId") String userId);
}
