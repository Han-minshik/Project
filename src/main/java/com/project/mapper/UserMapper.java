package com.project.mapper;

import com.project.dto.*;
import jakarta.validation.constraints.Pattern;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Mapper
public interface UserMapper {
    void createUser(UserDTO user);
    UserDTO getUserById(@Param("id") String id);
    UserDTO getUserByCi(@Param("ci") String ci);
    List<UserDTO> getAllUsers();
    void updateUser(UserDTO userDTO);
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
    void insertComplain(ComplainDTO complain);
    void deleteComplain(@Param("complainNo") Integer complainNo);
    String findIdByEmail(@Param("email") String email);
    List<ComplainDTO> getComplains();
    List<ComplainDTO> getMyComplains(@Param("userId") String userId);
    @Select("SELECT * FROM complain WHERE no = #{no}")
    ComplainDTO getComplainByNo(@Param("no") Integer no);
}
