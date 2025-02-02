package com.project.controller;

import com.project.dto.*;
import com.project.mapper.BookMapper;
import com.project.mapper.DiscussionMapper;
import com.project.service.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Controller
public class MainController {
    @Autowired private BookService bookService;
    @Autowired private DiscussionService discussionService;
    @Autowired private DiscussionCommentService discussionCommentService;
    @Autowired private UserService userService;
    @Autowired private LoanService loanService;

    @GetMapping("/")
    public String get_home (
            Model model,
            String userId
    ) {
        List<BookDTO> pBook5 = bookService.getPopularBook5();
        List<BookDTO> pBook = bookService.getPopularBook();
        List<DiscussionDTO> cDiscussion = discussionService.getCurrentDiscussions();
        DiscussionCommentDTO fComment = discussionCommentService.getFirstComment();
        DiscussionCommentDTO sComment = discussionCommentService.getSecondComment();
        List<BookDTO> pBook2 = bookService.getPopularBook2();
        UserDTO user = userService.find_user(userId);

        model.addAttribute("user", user);
        model.addAttribute("pBook5", pBook5);
        model.addAttribute("pBook", pBook);
        model.addAttribute("cDiscussion", cDiscussion);
        model.addAttribute("fComment", fComment);
        model.addAttribute("sComment", sComment);
        model.addAttribute("pBook2", pBook2);
        return "main/home";
    }
    /**************** 비밀번호 *********************/

    @GetMapping("/reset-pw")
    public String get_reset_pw() {
        return "user/reset-pw";
    }

    @GetMapping("/reset-pw-2")
    public String get_reset_pw_2(
            @RequestParam String code,
            HttpSession session,
            Model model
    ) {
        if(code.equals(session.getAttribute("code"))){
            String id = session.getAttribute("id").toString();
            log.info("reset-id :" + id);
            model.addAttribute("id", id);
            return "user/reset-pw-2";
        }
        return "user/reset-pw";
    }

    // 이것도 아마도 userRestController로 옮겨야 할듯
    @PostMapping("/reset-pw-2")
    public String post_reset_pw(
            HttpSession session,
            @RequestParam("password") String newPw
    ){
        // 패턴 검사도 함
        log.info("newPw: " + newPw);
        String id = session.getAttribute("id").toString();
        log.info("reset-2-id :" + id);
        boolean resetPwResult =  userService.reset_password(id, newPw);
        if (resetPwResult){
            return "redirect:/user/login";
        }
        return "user/reset-pw-2";
    }

    /************* 책 반환 ***********************/
    // ok
    @GetMapping("/book/book-category")
    public String getBooks(
            PageInfoDTO<BookDTO> pageInfo,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        // 쿠키에서 검색 키워드 가져오기
        String searchKeyword = getSearchKeywordFromCookies(request, response);

        PageInfoDTO<BookDTO> books;

        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            // 검색 조건이 있는 경우 검색 수행
            books = bookService.searchBooksByNameWithCount(pageInfo, searchKeyword);
        } else {
            // 검색 조건이 없는 경우 전체 데이터 조회
            books = bookService.getPaginatedBooks(pageInfo);
        }

        // 디버깅 로그 추가
        System.out.println("Search Keyword: " + searchKeyword);
        System.out.println("Books: " + books);

        // 모델에 데이터 추가
        model.addAttribute("books", books.getElements());
        model.addAttribute("totalCount", books.getTotalElementCount());
        model.addAttribute("pageInfo", books); // 검색 결과 반영된 PageInfo 전달
        model.addAttribute("isSearch", searchKeyword != null);

        return "book/book-category";
    }

    // ok(밑의 필드 두 개는 검색할 때 타임리프 작성의 편의를 위해 로직을 분리하고 템플릿에 삼항식으로 걸어둠)
    @GetMapping("/book/book-category/search")
    @ResponseBody
    public PageInfoDTO<BookDTO> searchBooksByName(
            @RequestParam String bookName,
            HttpServletResponse response
    ) {
        saveSearchKeywordToCookie(response, bookName);
        PageInfoDTO<BookDTO> books = bookService.searchBooksByNameWithCount(new PageInfoDTO<>(), bookName);
        books.setTotalElementCount(books.getTotalElementCount());
        return books;
    }

    // 쿠키에서 검색 키워드 가져오기
    private String getSearchKeywordFromCookies(HttpServletRequest request, HttpServletResponse response) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("searchKeyword".equals(cookie.getName())) {
                    String searchKeyword = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);

                    // ✅ 디버깅 로그 추가
                    log.info("🔍 검색 키워드 (디코딩 후): " + searchKeyword);

                    // ✅ 쿠키 삭제 (사용 후 제거)
                    Cookie deleteCookie = new Cookie("searchKeyword", null);
                    deleteCookie.setMaxAge(0);
                    deleteCookie.setPath("/");
                    response.addCookie(deleteCookie);

                    return searchKeyword;
                }
            }
        }
        return null;
    }


    // 검색 키워드를 쿠키에 저장
    private void saveSearchKeywordToCookie(HttpServletResponse response, String searchKeyword) {
        String encodedKeyword = URLEncoder.encode(searchKeyword, StandardCharsets.UTF_8);
        Cookie cookie = new Cookie("searchKeyword", encodedKeyword);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 5); // 쿠키 유효기간 설정 (5분)
        response.addCookie(cookie);
    }


    // 책 페이지 불러오기
    // ok
    @GetMapping("/book/{bookIsbn}")
    public String getBook(
            @PathVariable String bookIsbn,
            Model model
    ) {
        try {
            BookDTO book = bookService.getBookByIsbn(bookIsbn);
            if (book == null) {
                throw new IllegalArgumentException("책 정보를 찾을 수 없습니다: " + bookIsbn);
            }
            model.addAttribute("book", book);

            Integer bookDiscussionCount = bookService.getDiscussionCountByBookIsbn(bookIsbn);
            model.addAttribute("bookDiscussionCount", bookDiscussionCount);

            Integer bookParticipantCount = bookService.getParticipantCountByBookIsbn(bookIsbn);
            model.addAttribute("bookParticipantCount", bookParticipantCount);

            Integer availableCopies = loanService.getAvailableCopies(bookIsbn);
            model.addAttribute("availableCopies", availableCopies);

            LocalDateTime firstReturnDate = loanService.getFirstReturnDateByBookIsbn(bookIsbn);
            String formattedDate = (firstReturnDate != null) ? firstReturnDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "대출 가능일 없음";
            model.addAttribute("formattedDate", formattedDate);
            model.addAttribute("formattedDate", formattedDate);

            List<CategoryDTO> categories = bookService.getCategoryHierarchyByIsbn(bookIsbn);
            model.addAttribute("categories", categories);
            log.error(categories);
        } catch (Exception e) {
            log.error("Error fetching book data for ISBN: {}", bookIsbn, e);
            return "error/500";
        }
        return "book/book";
    }

    // 책 리뷰 불러오기
    // js로 태그 작성 같은걸 붙인다
    // ok
    @GetMapping("/book/{bookIsbn}/review")
    public String get_book_review (
            @PathVariable String bookIsbn,
            Model model,
            PageInfoDTO<ReviewDTO> pageInfo
    ){
        Map<String, Map<String, Object>> rateMap = bookService.getPaginatedReviews(pageInfo, bookIsbn);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("rateMap", rateMap);
        return "book/review-template";
    }

    /********************** 리뷰 댓글 추가 ****************/
    // ok
    @PostMapping("/book/{bookIsbn}/review/add")
    public ResponseEntity<String> addReview(
            Authentication auth,
            @PathVariable String bookIsbn,
            @RequestBody Map<String, Object> requestBody
    ) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        String userId = auth.getName(); // 현재 로그인된 사용자 ID
        String content = (String) requestBody.get("content");
        Integer rate;
        try {
            rate = Integer.parseInt(requestBody.get("rate").toString());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("별점은 숫자로 입력해야 합니다.");
        }
        if (content == null || content.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("리뷰 내용을 입력해주세요.");
        }
        if (rate < 0 || rate > 5) {
            return ResponseEntity.badRequest().body("별점은 1~5 사이여야 합니다.");
        }
        System.out.println("User: " + userId);
        System.out.println("Book ISBN: " + bookIsbn);
        System.out.println("Content: " + content);
        System.out.println("Rate: " + rate);
        try {
            bookService.insertReview(userId, bookIsbn, content, rate);
            return ResponseEntity.ok("리뷰가 성공적으로 작성되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류: 리뷰 저장 실패");
        }
    }

    /** 책 리뷰 좋아요 추가 **/
    // ok
    @PostMapping("/book/{bookIsbn}/review/like")
    public ResponseEntity<Integer> plusReviewLike(
            @PathVariable String bookIsbn,
            @RequestParam String content,
            @RequestParam String userId,  // 리뷰 작성자 ID 추가
            Authentication auth
    ) {
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer updatedRows = bookService.plusReviewLike(bookIsbn, content, userId);

        if (updatedRows > 0) {
            return ResponseEntity.ok(updatedRows);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
    }


    /********************* 토론 **********************/
    // ok
    @GetMapping("/discussion/category")
    public String getDiscussionCategory(
            Model model,
            PageInfoDTO<DiscussionDTO> pageInfo,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String searchKeyword = getSearchKeywordFromCookies(request, response);
        PageInfoDTO<DiscussionDTO> discussions;

        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            log.info("🔍 검색 실행: " + searchKeyword);
            discussions = discussionService.getDiscussionByBookTitle(pageInfo, searchKeyword);
            model.addAttribute("isSearch", true);
            model.addAttribute("searchKeyword", searchKeyword);
        } else {
            discussions = discussionService.getDiscussionsWithBookInfo(pageInfo);
            model.addAttribute("isSearch", false);
        }

        model.addAttribute("pageInfo", discussions);
        return "content/discussion-category";
    }



    // ok
    @GetMapping("/discussion/category/search")
    @ResponseBody
    public PageInfoDTO<DiscussionDTO> searchDiscussionByTitle(
            @RequestParam String bookName,
            PageInfoDTO<DiscussionDTO> pageInfo,
            HttpServletResponse response
    ) {
        // ✅ URL 디코딩 적용
        String decodedBookName = URLDecoder.decode(bookName, StandardCharsets.UTF_8);

        log.info("🔍 검색 요청 (디코딩 후): " + decodedBookName);

        // ✅ 쿠키 저장
        saveSearchKeywordToCookie(response, decodedBookName);

        return discussionService.getDiscussionByBookTitle(pageInfo, decodedBookName);
    }


    // 토론 페이지
    // ok
    @GetMapping("/discussion/{discussionId}")
    public String get_discussion (
            @PathVariable Integer discussionId,
            Model model
    ) {
        DiscussionDTO discussion = discussionService.selectDiscussionByDiscussionId(discussionId);
        model.addAttribute("discussion", discussion);
        return "content/discussion";
    }

    // 토론 페이지 댓글 불러오기
    // ok
    @GetMapping("/discussion/{discussionId}/comment")
    public String getDiscussionComment(
            @PathVariable Integer discussionId,
            Model model,
            PageInfoDTO<DiscussionCommentDTO> pageInfo
    ) {
        PageInfoDTO<DiscussionCommentDTO> paginatedDiscussionComment = discussionCommentService.getCommentsWithSortAndPagination(pageInfo, discussionId);

        model.addAttribute("paginatedDiscussionComment", paginatedDiscussionComment);
        model.addAttribute("pageInfo", pageInfo);
        Integer commentCount = discussionService.getCommentCountByDiscussion(discussionId);
        model.addAttribute("commentCount", commentCount);

        // ✅ Fragment만 반환하도록 변경
        return "content/discussion-comment :: comment-section";
    }


    // ok
    @PostMapping("/discussion/{discussionId}/comment/add")
    public ResponseEntity<String> postDiscussionCommentAdd(
            Authentication auth,
            @PathVariable Integer discussionId,
            @RequestBody DiscussionCommentDTO discussionComment
    ) {
        if (discussionComment == null) {
            return ResponseEntity.badRequest().body("❌ 요청 본문이 없습니다.");
        }

        log.error("🔍 서버에서 받은 discussionId: " + discussionId);
        log.error("🔍 서버에서 받은 content: " + discussionComment.getContent());

        if (auth == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        if (discussionId == null) {
            return ResponseEntity.badRequest().body("토론 ID가 필요합니다.");
        }

        if (discussionComment.getContent() == null || discussionComment.getContent().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("❌ 댓글 내용을 입력해주세요.");
        }

        String userId = auth.getName();
        discussionCommentService.addComment(discussionId, userId, discussionComment.getContent());

        return ResponseEntity.ok("댓글 추가 성공");
    }

    /****************** 좋아요 싫어요 *****************/
    // ok
    @PostMapping("/discussion/{discussionId}/comment/{commentId}/like")
    public ResponseEntity<Map<String, Integer>> post_comment_like(
            Authentication auth,
            @PathVariable Integer discussionId, // discussionId 추가
            @PathVariable Integer commentId // commentId
    ) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String userId = auth.getName();
        Map<String, Integer> response = new HashMap<>();

        try {
            // 좋아요 추가
            discussionCommentService.addLike(commentId, userId);

            // 업데이트된 좋아요와 싫어요 개수 가져오기
            Integer updatedLikes = discussionCommentService.getLikeCount(commentId);
            Integer updatedUnlikes = discussionCommentService.getUnlikeCount(commentId);

            response.put("like", updatedLikes);
            response.put("unlike", updatedUnlikes);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok(response);
    }

    // ok
    @PostMapping("/discussion/{discussionId}/comment/{commentId}/unlike")
    public ResponseEntity<Map<String, Integer>> post_comment_unlike(
            Authentication auth,
            @PathVariable Integer discussionId,
            @PathVariable Integer commentId
    ) {
        if (auth == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String userId = auth.getName();
        Map<String, Integer> response = new HashMap<>();

        try {
            discussionCommentService.addUnlike(commentId, userId);
            Integer updatedLikes = discussionCommentService.getLikeCount(commentId);
            Integer updatedUnlikes = discussionCommentService.getUnlikeCount(commentId);

            response.put("like", updatedLikes);
            response.put("unlike", updatedUnlikes);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // 응답이 JSON 형식으로 반환되도록 명확히 설정
        return ResponseEntity.ok(response);
    }




    /********************************************/
    // 토론 페이지 생성
    // ok
    @GetMapping("/discussion/add")
    public String get_discussion_add (
    ){
        return "user/write_talk";
    }

//    @PostMapping("/discussion/add")
//    public String post_discussion_add (
//            Authentication auth,
//            @RequestBody DiscussionDTO discussion
//
//    ){
//        if(auth != null){
//            String userId = auth.getName();
//            discussionService.createDiscussion(
//                    discussion.getBookTitle(),
//                    discussion.getTopic(),
//                    discussion.getContents(),
//                    userId,
//                    discussion.getBookIsbn()
//            );
//            return "content/discussion/category";
//        }
//        return "redirect:/user/login";
//
//    }

    /******************* 컴플레인(문의사항) ********************/
    @GetMapping("/complain")
    public String complain(Model model) {
        List<ComplainDTO> complains = userService.getComplains();
        model.addAttribute("complains", complains);
        return "user/complain";
    }

    @PostMapping("/complain/add")
    public String post_complain_add (
            Authentication auth,
            @RequestParam String title,
            @RequestParam String contents
    ){
        if(auth != null){
            userService.createComplain(title, contents, auth.getName());
            return "user/write_QA";
        }
        return "redirect:/user/login";
    }
}