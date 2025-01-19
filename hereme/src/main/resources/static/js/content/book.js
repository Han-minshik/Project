const discussionBtn = document.querySelector(".discussion-btn");
const rentBtn = document.querySelector(".rent-btn");

// 토론하러 가기 버튼을 눌렀을 때
discussionBtn.onclick = () => {
    if(confirm('이 책에 대한 토론에 참여하시겠습니까?')){
        location.href = "/discussion";
    }
}

// 대출하기 버튼을 눌렀을 때
rentBtn.onclick = () => {
    confirm('대출하시겠습니까?');
}