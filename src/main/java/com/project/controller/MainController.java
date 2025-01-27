package com.project.controller;

import com.project.dto.*;
import com.project.mapper.DiscussionMapper;
import com.project.service.*;
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
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
    @GetMapping("/resetPw")
    public String get_reset_pw() {return "user/reset-pw";}

    // 이것도 아마도 userRestController로 옮겨야 할듯
    @PostMapping("/resetPw")
    public String post_reset_pw(
            String id,
            @RequestParam("password") String newPw
    ){
        // 패턴 검사도 함
        boolean resetPwResult =  userService.reset_password(id, newPw);
        if (resetPwResult){
            return "redirect:/";
        }
        return "user/reset-pw";
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
    @GetMapping("/book/book-category")
    public String getBooks(
            PageInfoDTO<BookDTO> pageInfo,
            Model model
    ) {
        // 모든 책을 페이지네이션 처리하여 반환
        PageInfoDTO<BookDTO> books = bookService.getPaginatedBooks(pageInfo);

        // 모델에 데이터 추가
        model.addAttribute("books", books.getElements());
        model.addAttribute("totalCount", books.getTotalElementCount());
        model.addAttribute("pageInfo", pageInfo);

        return "book/book-category";
    }

    // 제목 검색 컨트롤러
    @GetMapping("/book/book-category/search")
    @ResponseBody
    public PageInfoDTO<BookDTO> searchBooksByName(
            PageInfoDTO<BookDTO> pageInfo,
            @RequestParam String bookName,
            Model model
    ) {
        PageInfoDTO<BookDTO> books = bookService.searchBooksByNameWithCount(pageInfo, bookName);
        pageInfo.setTotalElementCount(books.getTotalElementCount());
        model.addAttribute("pageInfo", pageInfo);
        return books;
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
            model.addAttribute("AvailableCopies", availableCopies);

            LocalDateTime firstReturnDate = loanService.getFirstReturnDateByBookIsbn(bookIsbn);
            model.addAttribute("firstReturnDate", firstReturnDate);

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
    @GetMapping("/discussion/category")
    public String getDiscussionCategory(
            Model model,
            PageInfoDTO<DiscussionDTO> pageInfo
    ) {
        List<DiscussionDTO> paginatedDiscussions = discussionService.getDiscussionsWithBookInfo(pageInfo);
        model.addAttribute("paginatedDiscussions", paginatedDiscussions);
        model.addAttribute("pageInfo", pageInfo);
        return "content/discussion-category";
    }

    @GetMapping("/discussion/category/search")
    @ResponseBody
    public PageInfoDTO<DiscussionDTO> searchDiscussionByTitle(
            @RequestParam String bookName,
            PageInfoDTO<DiscussionDTO> pageInfo,
            Model model
    ) {
        PageInfoDTO<DiscussionDTO> discussions = discussionService.getDiscussionByBookTitle(pageInfo, bookName);
        pageInfo.setTotalElementCount(discussions.getTotalElementCount());
        model.addAttribute("pageInfo", pageInfo);
        return discussions;
    }


    // 토론 페이지
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

    // 토론 페이지 댓글 불러오기 (어쩌면 fetch로 해야할지도)
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
        return "content/discussion-add";
    }

    @PostMapping("/discussion/add")
    public String post_discussion_add (
            Authentication auth,
            @RequestBody DiscussionDTO discussion

    ){
        if(auth != null){
            String userId = auth.getName();
            discussionService.createDiscussion(
                    discussion.getBookTitle(),
                    discussion.getTopic(),
                    discussion.getContents(),
                    userId,
                    discussion.getBookIsbn()
            );
            return "content/discussion-add";
        }
        return "redirect:/user/login";

    }

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
            return "content/discussion-comment-add";
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

    @PostMapping("/complain/add")
    public String post_complain_add (
            Authentication auth,
            @RequestParam String title,
            @RequestParam String contents
    ){
        if(auth != null){
            userService.createComplain(title, contents, auth.getName());
            return "content/complain-add";
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