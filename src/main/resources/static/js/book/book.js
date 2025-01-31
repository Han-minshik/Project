const bookForm = document.forms.namedItem('book');
const bookIsbn = bookForm.id;

const discussionBtn = document.querySelector(".discussion-btn");
const loanBtn = document.querySelector(".loan-btn");
const cartBtn = document.querySelector(".heart-btn");

const csrfToken = document.querySelector('meta[name=_csrf]').getAttribute('content'); // CSRF 토큰 가져오기

const reviewForm = document.getElementById('review-form')

discussionBtn.onclick = () => {
    const bookTitleElement = document.querySelector('h1'); // h1 태그 가져오기

    if (!bookTitleElement) {
        console.error("🚨 책 제목을 찾을 수 없습니다!");
        return;
    }

    let bookTitle = bookTitleElement.innerText.trim();

    if (bookTitle === "") {
        console.error("🚨 책 제목이 비어 있습니다.");
        return;
    }

    // 🔹 공백을 "-"(하이픈)으로 변환하여 URL과 쿠키에서 안전하게 사용
    bookTitle = bookTitle.replace(/\s+/g, "-"); // 모든 공백을 "-"로 변경

    // 🔹 URL 인코딩 적용
    const encodedBookTitle = encodeURIComponent(bookTitle);

    console.log(`📚 토론 검색 요청: ${bookTitle} -> /discussion/category/search?bookName=${encodedBookTitle}`);

    fetch(`/discussion/category/search?bookName=${encodedBookTitle}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`❌ 서버 응답 오류: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log("✅ 토론 검색 결과:", data);

            // 검색 결과가 있을 경우 해당 페이지로 이동
            if (data && data.elements && data.elements.length > 0) {
                location.href = `/discussion/category?bookName=${encodedBookTitle}`;
            } else {
                alert("❌ 해당 책에 대한 토론이 없습니다.");
            }
        })
        .catch(error => {
            console.error("❌ 오류 발생:", error);
            alert("토론 검색 중 오류가 발생했습니다.");
        });
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

    function initializeReviewForm() {
        const form = document.querySelector(".my-opinion-form");

        if (!form) {
            console.error("🚨 리뷰 작성 폼을 찾을 수 없습니다. 다시 시도합니다...");
            return;
        }

        console.log("✅ 리뷰 작성 폼을 찾았습니다!");

        form.addEventListener("submit", function (event) {
            event.preventDefault();

            const bookIsbn = document.forms.namedItem("book").id;
            const textArea = form.querySelector("textarea");
            const reviewContent = textArea.value.trim();
            const ratingValue = parseInt(document.getElementById("rating-value").value, 10);

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
                .then(response => response.text())
                .then(() => {
                    console.log("✅ 리뷰 추가 성공! 목록 업데이트");
                    updateReviewSection(bookIsbn);
                })
                .catch(error => console.error("❌ Error:", error));
        });

        // ⭐ 별점 선택 기능 추가
        const stars = document.querySelectorAll(".star-rating i");
        const ratingValue = document.getElementById("rating-value");

        stars.forEach(star => {
            star.addEventListener("click", function () {
                const value = this.getAttribute("data-value");
                ratingValue.value = value;

                // 클릭한 별과 그 이전 별들은 모두 채우기 (solid)
                stars.forEach((s, index) => {
                    if (index < value) {
                        s.classList.remove("fa-regular");
                        s.classList.add("fa-solid");
                    } else {
                        s.classList.remove("fa-solid");
                        s.classList.add("fa-regular");
                    }
                });
            });
        });

        // 👍 좋아요 버튼 클릭 이벤트 바인딩
        document.querySelectorAll(".review-recommend-section").forEach(button => {
            button.addEventListener("click", function () {
                const bookIsbn = document.forms.namedItem("book").id;
                const reviewContent = this.closest(".review").querySelector(".review-content").innerText.trim();
                const userId = this.closest(".review").querySelector(".review-user-name").innerText.trim();

                console.log(`👍 좋아요 요청: bookIsbn=${bookIsbn}, content=${reviewContent}, userId=${userId}`);

                fetch(`/book/${bookIsbn}/review/like?content=${encodeURIComponent(reviewContent)}&userId=${encodeURIComponent(userId)}`, {
                    method: "POST",
                    headers: {
                        "X-CSRF-TOKEN": document.querySelector("meta[name='_csrf']").getAttribute("content")
                    }
                })
                    .then(response => {
                        if (response.status === 401) {
                            alert("로그인이 필요합니다. 로그인 페이지로 이동합니다.");
                            window.location.href = "/user/login";
                            throw new Error("로그인이 필요합니다.");
                        }
                        return response.text();
                    })
                    .then(() => {
                        console.log("✅ 좋아요 성공! 리뷰 목록 업데이트");
                        updateReviewSection(bookIsbn);
                    })
                    .catch(error => console.error("❌ Error:", error));
            });
        });
    }

    function updateReviewSection(bookIsbn) {
        fetch(`/book/${bookIsbn}/review`)
            .then(response => response.text())
            .then(reviewTemplate => {
                reviewFormContainer.innerHTML = reviewTemplate;
                initializeReviewForm(); // 리뷰 폼 재초기화
            })
            .catch(error => console.error("❌ 리뷰 로딩 실패:", error));
    }

    const bookForm = document.forms.namedItem("book");
    if (bookForm) {
        const bookIsbn = bookForm.id;
        updateReviewSection(bookIsbn);
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