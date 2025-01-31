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

const deleteBtn = document.querySelector('.delete-selected-btn');
function deleteUser() {
    const checkboxes = document.querySelectorAll('.checklist1 tbody input[type="checkbox"]:checked');
    const userIds = Array.from(checkboxes).map(checkbox => {
        return checkbox.closest('tr').querySelector('td').textContent;
    });
    if(userIds.length === 0) {
        alert("사용자를 선택하세요.");
        return;
    }
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    fetch(`/admin/drop-user`, {
        method: 'DELETE',
        headers: {
            'X-CSRF-TOKEN': csrfToken,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({userIds: userIds})
    }).then(response => {
        if(!response.ok) {
            throw new Error('요청 실패');
        }
        return response.json();
    }).then(data => {
        console.log('삭제 성공 : ', data);
    }).catch(error => {
        console.error('오류 발생 : ', error);
    });
}
deleteBtn.onclick = deleteUser;

document.addEventListener("DOMContentLoaded", function () {
    const promoteBtn = document.querySelectorAll(".promote-btn");
    promoteBtn.forEach(button => {
        button.addEventListener("click", function () {
            const userRow = button.closest("tr");
            const userId = userRow.querySelector("td").textContent.trim();
            if(!userId) {
                alert("유저가 없습니다.");
                return;
            }
            const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
            fetch("/admin/update-user", {
                method: "PATCH",
                headers: {
                    "Content-Type": "application/json",
                    "X-CSRF-TOKEN": csrfToken
                },
                body: JSON.stringify({userId: userId})
            }).then(response => {
                if(!response.ok) {
                    return response.text().then(text => {throw new Error(text);});
                }
                return response.json();
            }).then(data => {
                alert("승격 완료");
                console.log("승격 완료 : ", data);
                userRow.style.backgroundColor = "#d4edda";
            }).catch(error => {
                console.error("오류 발생 : ", error);
                alert("실패했습니다 : " + error.message);
            })
        })
    })
})