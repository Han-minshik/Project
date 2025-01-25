const bookForm = document.forms.namedItem('book');
const discussionBtn = document.querySelector(".discussion-btn");
const loanBtn = document.querySelector(".loan-btn");
const heartBtn = document.querySelector(".heart-btn");
const reviewForm = document.getElementById('review-form');

// 책 ISBN 값
// const bookIsbn = document.getElementById('bookIsbn')?.value;

// 토론하러 가기 버튼을 눌렀을 때
// discussionBtn.onclick = () => {
//     location.href = `/discussion/book/${bookIsbn}`;
// };

// 찜하기 버튼을 눌렀을 때
heartBtn.onclick = () => {
    if (confirm('찜하시겠습니까?')) {
        const bookObject = createBookObject();
        request('/user/bookmark', bookObject.book).then(() => {
            if (confirm('찜했습니다. 찜한 내역을 확인하시겠습니까?')) {
                location.href = '/user/bookmarks';
            }
        });
    }
};

// 대출하기 버튼을 눌렀을 때
loanBtn.onclick = () => {
    if (confirm('대출하시겠습니까?')) {
        const bookObject = createBookObject();
        request('/user/loan', bookObject.loan).then(() => {
            if (confirm('대출이 완료되었습니다. 대출 내역을 확인하시겠습니까?')) {
                location.href = '/user/loans';
            }
        });
    }
};

// 책 객체 생성
function createBookObject() {
    return {
        book: {
            id: bookForm.id.value,
        },
        loan: {
            id: bookForm.id.value,
        }
    };
}

// API 요청 함수
function request(url, requestBody) {
    const csrfToken = document.querySelector('meta[name=_csrf]').getAttribute('content');
    return fetch(url, {
        method: "POST",
        headers: {
            "X-CSRF-TOKEN": csrfToken,
            "Content-Type": "application/json"
        },
        body: JSON.stringify(requestBody)
    }).then(response => {
        if (response.status === 401) {
            alert('로그인을 먼저 해주세요.');
            throw new Error('Unauthorized');
        } else if (!response.ok) {
            alert('시스템 에러가 발생했습니다.');
            throw new Error('System error');
        }
    });
}

// 리뷰 로딩 함수
const bookIsbn = bookForm.id;
load_review(null, `/book/${bookIsbn}/review`);
/// 상품에 대한 리뷰 불러오기
function load_review(event, url){
    if(event !== null){
        event.preventDefault();
    }
    reviewForm.innerHTML = '';
    fetch(url)
        .then(response => response.text())
        .then(reviewTemplate => {
            reviewForm.insertAdjacentHTML(`beforeend`, reviewTemplate)
        });
}
