const heartButton = document.querySelector('.book-heart-button');
const rentButton = document.querySelector('.book-rent-button');

// // 찜하기 버튼을 눌렀을 때
// heartButton.onclick = () => {
//     if (confirm('찜하시겠습니까?')){
//         if (confirm('찜 목록으로 이동하시겠습니까?')){
//             location.href = '/user/info';
//         }
//     }
// }
//
// // 대출하기 버튼을 눌렀을 때
// rentButton.onclick = () => {
//     if (confirm('대출하시겠습니까?')){
//         if (confirm('대출 목록으로 이동하시겠습니까?')){
//             location.href = '/user/info';
//         }
//     }
// }

// 찜하기 버튼을 눌렀을 때
heartButton.onclick = () => {
    go_to_info('찜');
}

// 대출하기 버튼을 눌렀을 때
rentButton.onclick = () => {
    go_to_info('대출');
}

function go_to_info(word){
    if(confirm(`${word}하시겠습니까?`)){
        if(confirm(`${word} 목록으로 이동하시겠습니까?`)){
            location.href = `/user/info`
        }
    }
}