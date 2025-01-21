const writeBtn = document.querySelector('.my-opinion-form button');

writeBtn.onclick = event => {
    event.preventDefault(); // 기본 폼 제출 방지

    const textArea = document.querySelector('.opinion-text');
    const commentText = textArea.value.trim();

    if (commentText !== "") {
        // 댓글 추가
        const discussionContainer = document.querySelector('.all-discussion');
        const commentCount = document.querySelector('.comment-count');

        const newComment = document.createElement('div');
        newComment.className = 'one-discussion';
        newComment.innerHTML = `
                <span>참여자1</span>
                <p>${commentText}</p>
                <div class="agree-disagree-btn">
                    <button class="agree">
                        <span>찬성</span>
                        <span>|</span>
                        <span>0</span>
                    </button>
                    <button class="disagree">
                        <span>반대</span>
                        <span>|</span>
                        <span>0</span>
                    </button>
                </div>
            `;

        discussionContainer.appendChild(newComment); // 댓글 추가
        commentCount.textContent = parseInt(commentCount.textContent) + 1 + '개'; // 댓글 수 업데이트
        textArea.value = "";
    }
}