const heartButton = document.querySelector('.book-heart-button');
const rentButton = document.querySelector('.book-heart-button');


const input = document.querySelector('.search-input');
const button = document.querySelector('.search-button');

/******************************************/

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
        location.href = '/book/book-category';
        return;
    }

    fetch(`/book/book-category/search?bookName=${encodeURIComponent(inputValue)}`, {
        method: 'GET',
    })
        .then(response => response.json())
        .then(data => {
            const resultDiv = document.querySelector('.all-book');
            const totalCountElement = document.getElementById('total-count');

            // 검색 결과 렌더링
            resultDiv.innerHTML = '';
            if (data.elements && data.elements.length > 0) {
                totalCountElement.textContent = data.totalElementCount; // 총 개수 업데이트
                data.elements.forEach(book => {
                    const searchBook = document.createElement('div');
                    searchBook.className = 'one-book';
                    searchBook.innerHTML = `
                        <div class="image-container">
                            <img src="${book.base64Image || '../../static/images/book_main.jpg'}" alt="${book.title}" />
                        </div>
                        <div class="book-info">
                            <h2>
                                <a href="/book/${book.isbn}">${book.title}</a>
                            </h2>
                            <div class="author-publisher">
                                <span class="author">${book.author}</span>
                                <span>/</span>
                                <span class="publisher">${book.publisher}</span>
                            </div>
                            <div class="rent-available">
                                <span>대출가능여부: </span>
                                <span class="rent-status">${book.copiesAvailable > 0 ? '가능' : '불가'}</span>
                            </div>
                        </div>
                    `;
                    resultDiv.appendChild(searchBook);
                });
            } else {
                totalCountElement.textContent = 0; // 검색 결과가 없을 경우
                resultDiv.innerHTML = '<p>검색 결과가 없습니다.</p>';
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('검색 중 문제가 발생했습니다. 나중에 다시 시도해주세요.');
        });
};

/**************************************/

// 찜하기 버튼을 눌렀을 때
heartButton.onclick = () => {
    if (confirm('찜하시겠습니까?')){
        if (confirm('찜 목록으로 이동하시겠습니까?')){
            location.href = '/user/info';
        }
    }
}

// 대출하기 버튼을 눌렀을 때
rentButton.onclick = () => {
    confirm('대출하시겠습니까?');
}

/*******************************************/

// 보기설정
const viewSizeSelect = document.getElementById('view-size-select');
viewSizeSelect.onchange = () => {
    const searchParams = new URLSearchParams(location.search);
    searchParams.set('size', viewSizeSelect.value);
    location.href = `/book/book-category?${searchParams.toString()}`;
}