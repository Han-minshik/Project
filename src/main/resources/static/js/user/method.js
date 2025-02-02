// <script>
//     var i, day = 20;
//     for (i = 10; i > 0; i--) {
//     document.write("<tr class='body'>")}
//     for (i = 10; i > 0; i--) {
//     document.write("<td>" + i +"</td>")}
//     for (i = 10; i > 0; i--) {
//     document.write("<td class='title'>제목입니다.</td>")}
//     for (i = 10; i > 0; i--) {
//     document.write("<td>작성자</td>")}
//     for (i = 10; i > 0; i--) {
//     document.write("<td>24-01-" + day-- + "</td>")}
//     for (i = 10; i > 0; i--) {
//     document.write("</tr>")}
// </script>
//
// function checkUserStatus(isLoggedIn) {
//     const writeButtonContainer = document.getElementById('writeButtonContainer');
//     if (isLoggedIn) {
//         writeButtonContainer.style.display = 'table-cell'; // 회원일 경우 보이게 설정
//     }
//     else {
//         writeButtonContainer.style.display = 'none'; // 비회원일 경우 숨기기
//     }
// }
//
// const userLoggedIn = true; // 예: 회원이면 true, 비회원이면 false
// checkUserStatus(userLoggedIn);
//
//
// const crewId = [[${crewId}]];
// const username = [[${#authentication.name}]];
//
// $(function() {
//     getComment();
// });

function loadHTML(elementId, file) {
    fetch(file)
        .then(response => response.text())
        .then(html => {
            document.getElementById(elementId).innerHTML = html;
        })
        .catch(err => console.warn('HTML 로딩 오류:', err));
}

// 페이지 로드 시 header와 footer를 불러오기
window.onload = function () {
    loadHTML('header-container', '/header');  // 서버에서 반환된 HTML 삽입
    loadHTML('footer-container', '/footer');  // 서버에서 반환된 HTML 삽입
};

document.addEventListener("DOMContentLoaded", function() {
    new Swiper(".swiper", {
        loop: true, // 무한 반복
        autoplay: {
            delay: 5000, // 5초마다 변경
            disableOnInteraction: false
        },
        speed: 5000, // 전환 속도
        effect: "slide", // 기본 슬라이드 효과 (부드러운 느낌)
        slidesPerView: 1, // 한 번에 한 개의 이미지만 보이도록 설정
        pagination: {
            el: ".swiper-pagination",
            clickable: true
        },
        navigation: {
            nextEl: ".swiper-button-next",
            prevEl: ".swiper-button-prev"
        }
    });
});