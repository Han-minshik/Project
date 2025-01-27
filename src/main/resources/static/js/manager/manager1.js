// 메뉴바 선택시 넘어가는 기능
document.addEventListener("DOMContentLoaded", function() {
    document.getElementById('menu1').addEventListener('click', function () {
        showContent(1);
    });
    document.getElementById('menu2').addEventListener('click', function () {
        showContent(2);
    });
    document.getElementById('menu3').addEventListener('click', function () {
        showContent(3);
    });
    document.getElementById('menu4').addEventListener('click', function () {
        showContent(4);
    });

    function showContent(menuNumber) {
        // 모든 콘텐츠 숨기기
        document.querySelectorAll('.content').forEach(function (content) {
            content.style.display = 'none';
        });

        // 선택된 메뉴에 해당하는 콘텐츠만 표시하기
        document.getElementById('content' + menuNumber).style.display = 'block';

        // 모든 메뉴에서 active 클래스 제거
        document.querySelectorAll('.ani-navbar-menu').forEach(function (menu) {
            menu.classList.remove('active');
        });

        // 클릭한 메뉴에 active 클래스 추가
        document.getElementById('menu' + menuNumber).classList.add('active');
    }
});

// 최근 회원정보 수정 1
document.addEventListener("DOMContentLoaded", function () {
    // 데이터를 동적으로 추가하는 부분
    const userData = [
        { nickname: "User4", correction:"닉네임을 수정함", dates: "25/24/23" },
        { nickname: "User5", correction:"계정탈퇴를함", dates: "24/23/22" },
        { nickname: "User6", correction:"비밀번호 변경", dates: "23/22/21" },
    ];

    const tableBody = document.querySelector(".checklist1 tbody");

    function renderTable(data) {
        tableBody.innerHTML = ""; // 기존 내용을 초기화
        data.forEach((item, index) => {
            const row = document.createElement("tr");

            const nicknameTd = document.createElement("td");
            nicknameTd.textContent = item.nickname;

            const correctionTd = document.createElement("td");
            correctionTd.textContent = item.correction;

            const datesTd = document.createElement("td");
            datesTd.textContent = item.dates;

            // 버튼 열(td) 생성
            const buttonTd = document.createElement("td");
            const checkbox = document.createElement("input");
            checkbox.type = "checkbox";
            checkbox.id = "check";
            buttonTd.appendChild(checkbox);

            // 행에 열 추가
            row.appendChild(nicknameTd);
            row.appendChild(correctionTd);
            row.appendChild(datesTd);
            row.appendChild(buttonTd);

            // tbody에 행 추가
            tableBody.appendChild(row);
        });
    }

    // // 테이블 렌더링
    // renderTable(userData);
    //
    // const deleteSelectedButton = document.querySelector('.delete-selected-btn');
    //
    // deleteSelectedButton.addEventListener('click', function() {
    //     const checkboxes = document.querySelectorAll('.checklist input[type="checkbox"]');
    //
    //     // 선택된 항목만 삭제
    //     checkboxes.forEach(checkbox => {
    //         if (checkbox.checked) {
    //             const listItem = checkbox.parentElement; // 체크박스가 있는 li 요소
    //             listItem.remove(); // 해당 항목 삭제
    //         }
    //     });
    // });

    // 초기 테이블 렌더링
    renderTable(userData);

    // 체크박스 상태 확인
    tableBody.addEventListener("click", (e) => {
        if (e.target.type === "checkbox") {
            const index = Array.from(tableBody.querySelectorAll("#check")).indexOf(e.target);
            if (e.target.checked) {
                alert(`${userData[index].nickname} 체크되었습니다!`);
            } else {
                alert(`${userData[index].nickname} 체크가 해제되었습니다.`);
            }
        }
    });

});

// 회원정보 수정 2
document.addEventListener("DOMContentLoaded", function () {
        // 데이터를 동적으로 추가하는 부분
        const userData = [
            { nickname: "User1", dates: "25/24/23" },
            { nickname: "User2", dates: "24/23/22" },
            { nickname: "User3", dates: "23/22/21" },
        ];

        const tableBody = document.querySelector(".checklist tbody");

        function renderTable(data) {
            tableBody.innerHTML = ""; // 기존 내용을 초기화
            data.forEach((item, index) => {
                // 행(tr) 생성
                const row = document.createElement("tr");

                // 닉네임 열(td) 생성
                const nicknameTd = document.createElement("td");
                nicknameTd.textContent = item.nickname;

                // 날짜 열(td) 생성
                const datesTd = document.createElement("td");
                datesTd.textContent = item.dates;

                // 버튼 열(td) 생성
                const buttonTd = document.createElement("td");
                const button = document.createElement("button");
                button.id = "levelup";
                button.textContent = "승급";
                buttonTd.appendChild(button);

                // 행에 열 추가
                row.appendChild(nicknameTd);
                row.appendChild(datesTd);
                row.appendChild(buttonTd);

                // tbody에 행 추가
                tableBody.appendChild(row);
            });
        }

        // 테이블 렌더링
        renderTable(userData);

        // 클릭 이벤트 처리
        tableBody.addEventListener("click", (e) => {
            if (e.target.id === "levelup") {
                const index = Array.from(tableBody.querySelectorAll("#levelup")).indexOf(e.target);
                alert(`${userData[index].nickname} 관리자로 전환되었습니다!`);
            }
        });

    });

// 책
document.addEventListener("DOMContentLoaded", function () {
    const userData = [
        { nickname: "해를 품는 달", dates: "50권" },
        { nickname: "사악한 늑대", dates: "1권" },
        { nickname: "고백", dates: "2권" },
    ];

    const tableBody = document.querySelector(".checklist2 tbody");

    function renderTable(data) {
        tableBody.innerHTML = ""; // 기존 내용 초기화
        data.forEach((item, index) => {
            const row = document.createElement("tr");
            row.dataset.index = index; // 행에 인덱스 정보 저장

            // 닉네임 열
            const nicknameTd = document.createElement("td");
            nicknameTd.textContent = item.nickname;

            // 날짜 열
            const datesTd = document.createElement("td");
            datesTd.textContent = item.dates;

            // 삭제 버튼 열
            const buttonTd = document.createElement("td");
            const button = document.createElement("button");
            button.id = "delete";
            button.textContent = "삭제";
            button.dataset.index = index; // 삭제할 데이터 인덱스 저장
            buttonTd.appendChild(button);

            row.appendChild(nicknameTd);
            row.appendChild(datesTd);
            row.appendChild(buttonTd);
            tableBody.appendChild(row);
        });
    }

    // 테이블 초기 렌더링
    renderTable(userData);

    // 삭제 버튼 클릭 이벤트
    tableBody.addEventListener("click", (e) => {
        if (e.target.id === "delete") {
            const index = parseInt(e.target.dataset.index, 10);
            const confirmDelete = confirm(`${userData[index].nickname} 정말 삭제하시겠습니까?`);
            if (confirmDelete) {
                userData.splice(index, 1); // 배열에서 데이터 삭제
                renderTable(userData); // 테이블 갱신
            }
            e.stopPropagation();
        }
    });

    // 행 클릭 이벤트 (같은 페이지로 이동하면서 데이터 전달)
    tableBody.addEventListener("click", (e) => {
        const row = e.target.closest("tr");
        if (row && row.dataset.index) {
            const index = parseInt(row.dataset.index, 10);
            const nickname = encodeURIComponent(userData[index].nickname); // URL에 전달할 데이터
            const dates = encodeURIComponent(userData[index].dates); // URL에 전달할 데이터
            window.location.href = `../../../../../../hereme/src/main/resources/templates/AllBook/category.html`;
        }
    });
});




// Q & A 리스트
/// 대댓글 입력창 추가 함수
function addReplyBox(questionId) {
    const replyBox = document.getElementById(`reply-${questionId}`);

    // 이미 댓글 입력창이 있다면 추가하지 않음
    if (replyBox.querySelector('.reply-form')) return;

    // 대댓글 입력창 생성
    const replyForm = document.createElement('div');
    replyForm.className = 'reply-form';
    replyForm.innerHTML = `
    <textarea placeholder="댓글을 입력하세요"></textarea>
    <button onclick="submitReply('${questionId}')">댓글 등록</button>
  `;

    // 대댓글 입력창을 댓글 영역에 추가
    replyBox.appendChild(replyForm);
}

// 댓글 등록 함수
function submitReply(questionId) {
    const replyBox = document.getElementById(`reply-${questionId}`);
    const textarea = replyBox.querySelector('textarea');

    if (textarea.value.trim() === '') {
        alert('댓글 내용을 입력하세요!');
        return;
    }

    // 댓글 내용 추가
    const replyContent = document.createElement('div');
    replyContent.className = 'reply-content';
    replyContent.textContent = textarea.value;

    // 댓글 영역에 댓글 내용을 추가
    replyBox.insertBefore(replyContent, replyBox.querySelector('.reply-form'));

    // 대댓글 입력창을 댓글 아래로 이동
    replyBox.appendChild(replyBox.querySelector('.reply-form'));

    // 입력창 초기화
    textarea.value = '';

    alert('댓글이 등록되었습니다!');
}
