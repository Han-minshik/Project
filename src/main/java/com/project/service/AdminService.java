package com.project.service;

import com.project.dto.AdminPostDTO;
import com.project.dto.BookDTO;
import com.project.dto.UserDTO;
import com.project.mapper.AdminMapper;
import com.project.mapper.BookMapper;
import com.project.mapper.UserMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class AdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BookMapper bookMapper;

    /**
     * 관리자 권한 확인
     */
    private boolean isAdmin(String userId) {
        UserDTO user = userMapper.getUserById(userId);
        return user != null && "관리자".equals(user.getRole());
    }

    /**
     * 관리자 권한 부여
     */
    public void promoteToAdmin(String adminId, String userId) {
        if (!isAdmin(adminId)) {
            throw new SecurityException("관리자 권한이 없습니다.");
        }

        adminMapper.createAdmin(userId);
        log.info("사용자 {}에게 관리자 권한을 부여했습니다.", userId);
    }

    /**
     * 최근 1주일 내 정보를 변경한 사용자 조회
     */
    public List<UserDTO> getRecentlyUpdatedUsers(String adminId) {
        if (!isAdmin(adminId)) {
            throw new SecurityException("관리자 권한이 없습니다.");
        }

        List<UserDTO> updatedUsers = adminMapper.getUpdatedUser();
        log.info("최근 1주일 동안 업데이트된 사용자 목록을 조회했습니다. 총 {}명의 사용자가 있습니다.", updatedUsers.size());
        return updatedUsers;
    }

    /**
     * 책 업데이트
     */
    public void updateBook(BookDTO book) {
        adminMapper.updateBook(book);
        log.info("책 정보를 업데이트했습니다 : {}", book.getTitle());
    }

    /**
     * 새로운 책 추가
     */
    public void insertBook(BookDTO book) {
        adminMapper.insertBook(book);
        log.info("책을 DB에 삽입했습니다 : {}", book.getTitle());
    }

    /**
     * 책 삭제
     */
    public void deleteBook(String adminId, String isbn) {
        if (!isAdmin(adminId)) {
            throw new SecurityException("관리자 권한이 없습니다.");
        }

        adminMapper.deleteBook(isbn);
        log.info("책을 삭제했습니다 : {}", isbn);
    }

    /**
     * 관리자 공지사항 추가
     */
    public void addAdminPost(String adminId, AdminPostDTO adminPost) {
        if (!isAdmin(adminId)) {
            throw new SecurityException("관리자 권한이 없습니다.");
        }

        adminMapper.createAdminPost(adminPost);
        log.info("공지사항을 DB에 삽입했습니다 : {}", adminPost.getTitle());
    }

    /**
     * 특정 공지사항 조회
     */
    public AdminPostDTO getAdminPostById(String adminId, Integer postId) {
        if (!isAdmin(adminId)) {
            throw new SecurityException("관리자 권한이 없습니다.");
        }

        List<AdminPostDTO> posts = adminMapper.getAdminPostById(postId);
        if (posts == null || posts.isEmpty()) {
            log.warn("ID {}에 해당하는 공지사항이 존재하지 않습니다.", postId);
            return null;
        }

        AdminPostDTO post = posts.get(0); // 첫 번째 요소 선택
        log.info("공지사항을 조회했습니다: {}", post.getTitle());
        return post;
    }


    /**
     * 모든 공지사항 조회
     */
    public List<AdminPostDTO> getAllAdminPosts(String adminId) {
        if (!isAdmin(adminId)) {
            throw new SecurityException("관리자 권한이 없습니다.");
        }

        List<AdminPostDTO> posts = adminMapper.getAllAdminPosts();
        log.info("모든 공지사항을 조회했습니다. 총 {}개의 공지사항이 있습니다.", posts.size());
        return posts;
    }

    /**
     * 공지사항 업데이트
     */
    public void updateAdminPost(String adminId, AdminPostDTO adminPost) {
        if (!isAdmin(adminId)) {
            throw new SecurityException("관리자 권한이 없습니다.");
        }

        adminMapper.updateAdminPost(adminPost);
        log.info("공지사항을 업데이트했습니다 : {}", adminPost.getTitle());
    }

    /**
     * 공지사항 삭제
     */
    public void deleteAdminPost(String adminId, Integer postId) {
        if (!isAdmin(adminId)) {
            throw new SecurityException("관리자 권한이 없습니다.");
        }

        adminMapper.deleteAdminPost(postId);
        log.info("공지사항을 삭제했습니다. ID: {}", postId);
    }
}
