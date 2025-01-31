// 메뉴바 선택 시 해당 콘텐츠 표시
document.addEventListener("DOMContentLoaded", function() {
    document.querySelectorAll('.content').forEach(function (content) {
        content.style.display = 'none'; // 초기에는 모든 콘텐츠 숨김
    });

    document.getElementById('menu1').addEventListener('click', function () {
        showContent(1);
    });
    document.getElementById('menu2').addEventListener('click', function () {
        showContent(2);
    });
    document.getElementById('menu3').addEventListener('click', function () {
        showContent(3);
    });

    function showContent(menuNumber) {
        // 모든 콘텐츠 숨기기
        document.querySelectorAll('.content').forEach(function (content) {
            content.style.display = 'none';
        });

        // 선택된 메뉴에 해당하는 콘텐츠만 표시
        document.getElementById('content' + menuNumber).style.display = 'block';

        // 모든 메뉴에서 active 클래스 제거
        document.querySelectorAll('.ani-navbar-menu').forEach(function (menu) {
            menu.classList.remove('active');
        });

        // 클릭한 메뉴에 active 클래스 추가
        document.getElementById('menu' + menuNumber).classList.add('active');
    }
});
