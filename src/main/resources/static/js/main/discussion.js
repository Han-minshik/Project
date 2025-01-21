const discussionBtn = document.querySelector('.discussion-button')
const bookBtn = document.querySelector('.book-button')

const viewSizeSelect = document.getElementById('view-size-select');

// 토론하러 가기 버튼을 눌렀을 때
discussionBtn.onclick = () => {
    const isbn = document.getElementById('bookIsbn').value; // hidden input에서 ISBN 값 가져오기
    location.href = `/book/${isbn}`;
}

// 책 보러가기 버튼을 눌렀을 때
bookBtn.onclick = () => {
    const isbn = document.getElementById('bookIsbn').value; // hidden input에서 ISBN 값 가져오기
    location.href = `/book/${isbn}/discussions`; // ISBN을 포함한 URL로 이동
}

/*******************************************/

// 보기설정
viewSizeSelect.onchange = () => {
    const searchParams = new URLSearchParams(location.search);
    searchParams.set('size', viewSizeSelect.value);
    location.href = `/book?${searchParams.toString()}`;
}