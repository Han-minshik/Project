const discussionBtn = document.querySelector('.discussion-button')
const bookBtn = document.querySelector('.book-button');

const input = document.querySelector('.search-input');
const button = document.querySelector('.search-button');

// 검색하기
// enter 키 눌렀을 때
input.onkeypress = (event) => {
    if (event.key === 'Enter') {
        event.preventDefault(); // 기본 폼 제출 방지
        executeSearch(); // 검색 함수 호출
    }
};

// 돋보기 아이콘 눌렀을 때
button.onclick = () => {
    executeSearch(); // 검색 함수 호출
};

const executeSearch = () => {
    const input = document.querySelector('.search-input');
    const inputValue = input.value.trim();

    if (!inputValue) {
        // 검색어가 없을 경우 기본 페이지로 이동
        location.href = '/discussion/category';
        return;
    }

    fetch(`/discussion/category/search?bookName=${encodeURIComponent(inputValue)}`, {
        method: 'GET',
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`API 요청 실패: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            const resultDiv = document.querySelector('.all-book-discussion');
            const totalCountElement = document.getElementById('total-count');

            // 검색 결과 렌더링
            resultDiv.innerHTML = '';
            if (data.elements && data.elements.length > 0) {
                totalCountElement.textContent = data.totalElementCount; // 총 개수 업데이트
                data.elements.forEach(discussion => {
                    const searchBook = document.createElement('div');
                    searchBook.className = 'one-book-discussion';
                    searchBook.innerHTML = `
                        <div class="image-container">
                            <img src="${discussion.base64Image || '../../static/images/book_main.jpg'}" alt="메인이미지" />
                        </div>
                        <div class="discussion-title-recent">
                            <h2>
                                <a href="/discussion/${discussion.id}">${discussion.bookTitle}</a>
                            </h2>
                            <div class="discussion-title">
                                <span>토론 주제:</span>
                                <p>${discussion.topic}</p>
                            </div>
                            <div class="discussion-recent">
                                <p>${discussion.recentComment || '최근 댓글 없음'}</p>
                            </div>
                            <div class="discussion-button-section">
                                <button class="book-button">
                                    <a href="/book/${discussion.bookIsbn}">책 보러가기</a>
                                </button>
                                <button class="discussion-button">
                                    <a href="/discussion/${discussion.id}">토론 참여하기</a>
                                </button>
                            </div>
                        </div>
                    `;
                    resultDiv.appendChild(searchBook);
                });
            } else {
                totalCountElement.textContent = 0; // 검색 결과 없음
                resultDiv.innerHTML = '<p>검색 결과가 없습니다.</p>';
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('검색 중 문제가 발생했습니다. 나중에 다시 시도해주세요.');
        });
};

// 검색 버튼 클릭 이벤트
const searchButton = document.querySelector('.search-button');
searchButton.onclick = executeSearch;

// Enter 키로 검색
const searchInput = document.querySelector('.search-input');
searchInput.onkeypress = (event) => {
    if (event.key === 'Enter') {
        event.preventDefault();
        executeSearch();
    }
};


// // 토론하러 가기 버튼을 눌렀을 때
// discussionBtn.onclick = () => {
//     const isbn = document.getElementById('bookIsbn').value; // hidden input에서 ISBN 값 가져오기
//     location.href = `/book/${isbn}`;
// }
//
// /**********************************/
//
// // 책 보러가기 버튼을 눌렀을 때
// bookBtn.onclick = () => {
//     const isbn = document.getElementById('bookIsbn').value; // hidden input에서 ISBN 값 가져오기
//     location.href = `/book/${isbn}/discussions`; // ISBN을 포함한 URL로 이동
// }

/*******************************************/

// 보기설정
const viewSizeSelect = document.getElementById('view-size-select');
viewSizeSelect.onchange = () => {
    const searchParams = new URLSearchParams(location.search);
    searchParams.set('size', viewSizeSelect.value);
    location.href = `/book?${searchParams.toString()}`;
}