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
    const reviewFormContainer = document.getElementById("review-form");

    // ⭐ 리뷰 폼 초기화
    function initializeReviewForm() {
        const form = document.querySelector(".my-opinion-form");

        if (!form) {
            console.warn("⏳ 리뷰 작성 폼을 찾을 수 없습니다. 대기 중...");
            return;
        }

        console.log("✅ 리뷰 작성 폼을 찾았습니다!");

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
                const value = parseInt(this.getAttribute("data-value"), 10);
                ratingInput.value = value;
                updateStars(value);
            });

            star.addEventListener("mouseover", function () {
                updateStars(parseInt(this.getAttribute("data-value"), 10));
            });

            star.addEventListener("mouseleave", function () {
                updateStars(parseInt(ratingInput.value) || 0);
            });
        });

        // ⭐ 폼 제출 이벤트 (AJAX 요청)
        form.addEventListener("submit", function (event) {
            event.preventDefault();

            const bookForm = document.forms.namedItem("book");
            if (!bookForm) {
                console.error("🚨 책 정보를 찾을 수 없습니다.");
                return;
            }

            const bookIsbn = bookForm.id;
            const textArea = form.querySelector("textarea");
            const reviewContent = textArea.value.trim();
            const ratingValue = parseInt(ratingInput.value, 10);

            if (reviewContent === "") {
                alert("리뷰 내용을 입력해주세요.");
                return;
            }

            if (!ratingValue || ratingValue < 1 || ratingValue > 5) {
                alert("별점을 올바르게 선택해주세요.");
                return;
            }

            const reviewData = { content: reviewContent, rate: ratingValue };
            console.log("📤 보낼 데이터:", JSON.stringify(reviewData));

            fetch(`/book/${bookIsbn}/review/add`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "X-CSRF-TOKEN": document.querySelector("meta[name='_csrf']").getAttribute("content")
                },
                body: JSON.stringify(reviewData)
            })
                .then(response => {
                    if (response.ok) {
                        console.log("✅ 리뷰 작성 완료, 최신 리뷰 불러오는 중...");
                        return fetch(`/book/${bookIsbn}/review`);
                    } else {
                        throw new Error("리뷰 작성에 실패했습니다.");
                    }
                })
                .then(response => response.text())
                .then(reviewHtml => {
                    console.log("🔄 최신 리뷰 업데이트 완료!");

                    reviewFormContainer.innerHTML = reviewHtml;
                    initializeReviewForm(); // 리뷰 폼 재초기화
                })
                .catch(error => console.error("❌ Error:", error));
        });
    }

    // ⭐ 리뷰 목록 불러오기
    function load_review(event, url) {
        if (event) event.preventDefault();

        console.log("🔄 리뷰 불러오는 중:", url);

        fetch(url)
            .then(response => response.text())
            .then(reviewTemplate => {
                reviewFormContainer.innerHTML = reviewTemplate;
                initializeReviewForm(); // 리뷰 폼 초기화
            })
            .catch(error => console.error("❌ 리뷰 로딩 실패:", error));
    }

    // 📌 `review-form`이 변경될 때 자동으로 감지하여 `initializeReviewForm()` 실행
    const observer = new MutationObserver(() => {
        if (document.querySelector(".my-opinion-form")) {
            observer.disconnect();
            initializeReviewForm();
        }
    });

    if (reviewFormContainer) {
        observer.observe(reviewFormContainer, { childList: true, subtree: true });
    }

    // 📌 페이지 로딩 시 리뷰 자동 로드
    const bookForm = document.forms.namedItem("book");
    if (bookForm) {
        const bookIsbn = bookForm.id;
        load_review(null, `/book/${bookIsbn}/review`);
    }
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