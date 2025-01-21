package com.project.mapper;

import com.project.dto.ComplainDTO;
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
    String getTopCommentUserByDiscussionId(Integer discussionId);
    Integer deductPoints(@Param("userId") String userId, @Param("points") Integer points);
    Integer getCommentCountByUserInDiscussion(@Param("userId") String userId, @Param("discussionId") Integer discussionId);
    List<Integer> getAllDiscussionsByUser(String userId);
    void insertComplain(ComplainDTO userId);
    void updateComplain(ComplainDTO complain);
    void deleteComplain(Integer complainNo);
    String findIdByEmail(String email);

}
