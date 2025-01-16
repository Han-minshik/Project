const discussionBtn = document.querySelector('.discussion-button')
const bookBtn = document.querySelector('.book-button')

// 토론하러 가기 버튼을 눌렀을 때
discussionBtn.onclick = () => {
    location.href="/participation";
}

// 책 보러가기 버튼을 눌렀을 때
bookBtn.onclick = () => {
    location.href="/book";
}