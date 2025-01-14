const reviewContainer = document.querySelector('.my-opinion-container');
const reviewForm = reviewContainer.querySelector('form');
const discussionBtn = document.querySelector('.discussion-button')
const [reviewWriteBtn, reviewCloseBtn] = reviewForm.querySelectorAll('.button-container button');

discussionBtn.onclick = () => {
    reviewContainer.style.display = 'block';
}

reviewCloseBtn.onclick = () => {
    reviewContainer.style.display = 'none';
}