const heartButton = document.querySelector('.book-heart-button');
const rentButton = document.querySelector('.book-heart-button');

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