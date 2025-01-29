const bookForm = document.forms.namedItem('book');
const bookIsbn = bookForm.id;

const discussionBtn = document.querySelector(".discussion-btn");
const loanBtn = document.querySelector(".loan-btn");
const cartBtn = document.querySelector(".heart-btn");

const csrfToken = document.querySelector('meta[name=_csrf]').getAttribute('content'); // CSRF 토큰 가져오기

const reviewForm = document.getElementById('review-form')

discussionBtn.onclick = () => {
    const bookTitle = document.querySelector('h1').innerText.trim(); // 동적으로 책 제목 가져오기

    // 책 제목을 기반으로 토론 목록 페이지로 이동
    const encodedBookTitle = encodeURIComponent(bookTitle);
    location.href = `/discussion/category?bookName=${encodedBookTitle}`;
};

/*******************************************/

// 찜하기 버튼을 눌렀을 때
cartBtn.onclick = () => {
    const cartObject = createCartObject();
    console.log("bookForm:", bookForm); // bookForm 객체 출력
    console.log("isbn:", bookForm?.id); // id 값 출력

    if (!cartObject || !cartObject.isbn) {
        alert('책 정보를 가져올 수 없습니다.');
        return;
    }

    if (confirm('찜하시겠습니까?')) {
        // 서버 요청
        fetch(`/user/wishlist/add`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': csrfToken, // CSRF 토큰 추가
            },
            body: JSON.stringify(cartObject), // JSON 형식으로 요청 본문 설정
        })
            .then(response => {
                if (!response.ok) {
                    if (response.status === 401) {
                        throw new Error('로그인이 필요합니다.');
                    }
                    throw new Error('찜하기 요청 실패');
                }
                return response.json(); // 응답 처리
            })
            .then(cartNo => {
                if (confirm(`찜했습니다. 장바구니 번호: ${cartNo}. 찜한 내역을 확인하시겠습니까?`)) {
                    location.href = '/user/wishlist'; // 찜한 목록 페이지로 이동
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert(error.message || '찜 요청 처리 중 문제가 발생했습니다. 다시 시도해주세요.');
            });
    }
};

// 대출하기 버튼을 눌렀을 때
loanBtn.onclick = () => {
    if (confirm('대출하시겠습니까?')) {
        const bookObject = createLoanObject();

        request('/user/my-page', bookObject.loan).then(() => {
            if (confirm('대출이 완료되었습니다. 대출 내역을 확인하시겠습니까?')) {
                location.href = '/user/my-page';
            }
        });
    }
};


function createCartObject() {
    const bookForm = document.forms.namedItem('book'); // 폼 요소 선택
    if (!bookForm) {
        console.error("bookForm을 찾을 수 없습니다.");
        return null;
    }

    const bookTitle = document.querySelector('h1').innerText.trim(); // 책 제목
    const bookAuthor = document.querySelector('h2').innerText.split('/')[0].trim(); // 저자
    const bookPublisher = document.querySelector('h2').innerText.split('/')[1]?.trim(); // 출판사
    const bookPrice = document.querySelector('.book-price span:nth-of-type(2)')?.innerText?.replace(/[^0-9]/g, ''); // 가격

    return {
        isbn: bookForm.id,
        title: bookTitle || null,
        author: bookAuthor || null,
        publisher: bookPublisher || null,
        price: parseInt(bookPrice, 10) || 0,
    };
}


// 찜/대출하기 페이지에 상품 추가하는 요청
function request(url, requestBody){
    const csrfToken = document.querySelector('meta[name=_csrf]').getAttribute('content');
    // 대출하기에 POST 요청 전송
    return fetch(url, {
        method: "POST",
        headers: {
            "X-CSRF-TOKEN": csrfToken,
            "Content-Type": "application/json"
        },
        body: JSON.stringify(requestBody)
    }).then(response => {
        // 로그인이 안된 유저가 클릭 시
        if(response.status === 401){
            alert('로그인을 먼저 해주세요');
            throw new Error();
        }
        else if(!response.ok){
            alert('시스템 에러 발생!');
            throw new Error();
        }
    });
}

/**************************************/
document.addEventListener("DOMContentLoaded", function () {
    let form;

    function initializeReviewForm() {
        form = document.querySelector(".my-opinion-form");

        if (!form) {
            console.warn("리뷰 작성 폼을 찾을 수 없습니다. 동적 로딩을 기다립니다...");
            return;
        }

        const stars = document.querySelectorAll(".star-rating i");
        const ratingInput = document.getElementById("rating-value");

        function updateStars(value) {
            stars.forEach((star, index) => {
                if (index < value) {
                    star.classList.remove("fa-regular");
                    star.classList.add("fa-solid");
                } else {
                    star.classList.remove("fa-solid");
                    star.classList.add("fa-regular");
                }
            });
        }

        // ⭐ 별점 선택 이벤트
        stars.forEach((star) => {
            star.addEventListener("click", function () {
                const value = parseInt(this.getAttribute("data-value"));
                ratingInput.value = value; // hidden input에 값 저장
                updateStars(value);
            });

            // 마우스 오버 효과
            star.addEventListener("mouseover", function () {
                const value = parseInt(this.getAttribute("data-value"));
                updateStars(value);
            });

            // 마우스가 별점 영역을 벗어나면 선택된 별점만 유지
            star.addEventListener("mouseleave", function () {
                updateStars(parseInt(ratingInput.value) || 0);
            });
        });

        // 폼 제출 이벤트 (AJAX 요청)
        form.addEventListener("submit", function (event) {
            event.preventDefault(); // 기본 폼 제출 방지

            const bookIsbn = window.location.pathname.split("/")[2]; // 현재 URL에서 ISBN 추출
            const textArea = form.querySelector("textarea");
            const reviewContent = textArea.value.trim();
            const ratingValue = ratingInput.value; // 선택한 별점 값

            if (reviewContent === "") {
                alert("리뷰 내용을 입력해주세요.");
                return;
            }

            if (ratingValue === "0") {
                alert("별점을 선택해주세요.");
                return;
            }

            fetch(`/book/${bookIsbn}/review/add`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "X-CSRF-TOKEN": document.querySelector("meta[name='_csrf']").getAttribute("content")
                },
                body: JSON.stringify({ content: reviewContent, rate: ratingValue })
            })
                .then(response => {
                    if (response.ok) {
                        return response.text();
                    } else {
                        throw new Error("리뷰 작성에 실패했습니다.");
                    }
                })
                .then(html => {
                    const reviewSection = document.getElementById("review-form");
                    reviewSection.innerHTML = html; // 새로운 리뷰 템플릿 업데이트
                    initializeReviewForm(); // 새롭게 추가된 폼에도 이벤트 바인딩
                })
                .catch(error => console.error("Error:", error));
        });
    }

    // 📌 MutationObserver를 사용하여 `.my-opinion-form`이 동적으로 추가될 때 감지
    const observer = new MutationObserver(() => {
        if (document.querySelector(".my-opinion-form")) {
            observer.disconnect();
            initializeReviewForm();
        }
    });

    observer.observe(document.body, { childList: true, subtree: true });

    // 초기 실행 (기본 폼이 있는 경우)
    initializeReviewForm();
});


/**************************************/
/// 상품에 대한 리뷰 불러오기
load_review(null, `/book/${bookIsbn}/review`);
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