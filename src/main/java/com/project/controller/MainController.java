package com.project.controller;

import com.project.dto.*;
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
    @Autowired
    private DiscussionMapper discussionMapper;

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
    /*************************************/

    // 비밀번호 분실
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

//    @GetMapping("/book/book-category")
//    public String getBooks(
//            PageInfoDTO<BookDTO> pageInfo,
//            @RequestParam(required = false) String bookName,
//            Model model
//    ) {
//        PageInfoDTO<BookDTO> books;
//        if(bookName != null && !bookName.trim().isEmpty()) {
//            books = bookService.searchBooksByNameWithCount(pageInfo, bookName);
//        }
//        else {
//            books = bookService.getPaginatedBooks(pageInfo);
//        }
//        model.addAttribute("books", books.getElements());
//        model.addAttribute("totalCount", books.getTotalElementCount());
//        model.addAttribute("bookName", bookName);
//        model.addAttribute("pageInfo", pageInfo);
//        return "book/book-category";
//    }

    // 전체 책 반환 컨트롤러


    // 모든 책 목록
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
                    String searchKeyword = cookie.getValue();

                    // 쿠키 삭제
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
        Cookie cookie = new Cookie("searchKeyword", searchKeyword);
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
    @PostMapping("/book/{bookIsbn}/review/add")
    public String add_review (
            Authentication auth,
            @PathVariable String bookIsbn
    ){
        return "redirect:/book/" + bookIsbn; // not yet
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
            // 검색어가 있을 경우 검색 수행
            discussions = discussionService.getDiscussionByBookTitle(pageInfo, searchKeyword);
            model.addAttribute("isSearch", true);
            model.addAttribute("searchKeyword", searchKeyword);
        } else {
            // 검색어가 없을 경우 기본 리스트 반환
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
        saveSearchKeywordToCookie(response, bookName);
        return discussionService.getDiscussionByBookTitle(pageInfo, bookName);
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
        log.error(discussion);
        return "content/discussion";
    }

    // 토론 페이지 댓글 불러오기 (test 예정)
    @GetMapping("/discussion/{discussionId}/comment")
    public String get_discussion_comment(
            @PathVariable Integer discussionId,
            Model model,
            PageInfoDTO<DiscussionCommentDTO> pageInfo
    ){
        PageInfoDTO<DiscussionCommentDTO> paginatedDiscussionComment = discussionCommentService.getCommentsWithSortAndPagination(new PageInfoDTO<>() ,discussionId);
        model.addAttribute("paginatedDiscussionComment-comment", paginatedDiscussionComment);
        Integer commentCount = discussionService.getCommentCountByDiscussion(discussionId);
        model.addAttribute("commentCount", commentCount);
        model.addAttribute("pageInfo", pageInfo);
        return "content/discussion-comment";
    }

    /********************************************/
    // 토론 페이지 생성
    @GetMapping("/discussion/add")
    public String get_discussion_add (
    ){
        return "user/write_talk";
    }

//    @PostMapping("/discussion/add") -> MainRestController로 이동


    // 토론 댓글 생성
    @PostMapping("/comment/add")
    public String post_discussion_comment_add (
            Authentication auth,
            @RequestParam Integer discussionId,
            @RequestBody DiscussionCommentDTO discussionComment

    ){
        if(auth != null){
            String userId = auth.getName();
            discussionCommentService.addComment(discussionId, userId, discussionComment.getContent());
            return "redirect:/discussion/" + discussionId;
        }
        return "redirect:/user/login";

    }

    /****************** 좋아요 싫어요 *****************/
    @GetMapping("/comment/{commentId}/like")
    public String get_comment_like(
            Authentication auth,
            @PathVariable Integer commentId
    ){
        if(auth != null){
            discussionCommentService.addLike(commentId, auth.getName());
            return "content/discussion-comment-like";
        }
        return "redirect:/user/login";

    }

    @GetMapping("/comment/{commentId}/unlike")
    public String get_comment_unlike(
            Authentication auth,
            @PathVariable Integer commentId
    ){
        if(auth != null){
            discussionCommentService.addUnlike(commentId, auth.getName());
            return "content/discussion-comment-unlike";
        }
        return "redirect:/user/login";

    }

    /******************* 컴플레인(문의사항) ********************/
    @GetMapping("/complain")
    public String complain(Model model) {
        List<ComplainDTO> complains = userService.getComplains();
        model.addAttribute("complains", complains);
        return "user/complain";
    }

    @GetMapping("/complain/add")
    public String get_complain_add(){
        return "user/write_QA";
    }

    @PostMapping("/complain/add")
    public String post_complain_add (
            Authentication auth,
            @RequestParam String title,
            @RequestParam String contents
    ){
        if(auth != null){
            userService.createComplain(title, contents, auth.getName());
            return "redirect:/complain";
        }
        return "redirect:/user/login";
    }

//    @PatchMapping("/complain/update")
//    public String patch_complain_update (
//            Authentication auth,
//            @RequestParam Integer compainId
//    ){
//        if(auth != null){
//            userService.updateComplain(compainId, auth.getName());
//            return "content/complain-update";
//        }
//        return "redirect:/user/login";
//    }
//
//    @DeleteMapping("/complain/delete")
//    public String delete_complain_delete (
//            Authentication auth,
//            @RequestParam Integer compainId
//    ){
//        if(auth != null){
//            userService.deleteComplain(compainId);
//            return "content/complain-delete";
//        }
//        return "redirect:/user/login";
//    }


}