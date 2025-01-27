const idInput = document.getElementById('id');
const pwInput = document.getElementById('password');
const pwReInput = document.getElementById('password-re');
const idCheckBtn = document.querySelector('.id-input-container button')

const idCheckInput = document.querySelector('input[name="id-check"]');
const idCheckValidInput = document.querySelector('input[name="id-check-valid"]');

const telHead = document.querySelector('.tel-input-container select');
const [telMiddle, telTail] = document.querySelectorAll('.tel-input-container input');
const telAuthBtn = document.querySelector('.tel-input-container button');
const [emailHead, emailTail] = document.querySelectorAll('.email-input-container input');
const emailSelect = document.querySelector('.email-input-container select');
const emailAuthBtn = document.querySelector('.email-input-container button');
const emailAuthConfirmInput = document.querySelector('.email-auth-input-container input');
const emailAuthConfirmBtn = document.querySelector('.email-auth-input-container button');
const [joinBtn, cancelBtn] = document.querySelectorAll('.join-btn-section > button');
const [idInputError, pwInputError, telInputError, emailInputError] = document.querySelectorAll('.error-container');

let telAuthCompleted = false; // 휴대폰 인증 여부
let emailAuthCompleted = false; // 이메일 인증 여부
/************************************************************************/
// id 중복 체크 버튼 클릭 시
idCheckBtn.onclick = () => {
    const id = idInput.value;
    if(id.trim() === ''){
        alert('ID를 입력해주세요!');
        return;
    }
    fetch(`/user/id/${id}`)
        .then(response => {
            idCheckInput.value = id;
            idCheckValidInput.value = false;

            switch (response.status){
                case 200:
                    alert('해당 아이디는 사용 가능합니다^-^');
                    idCheckValidInput.value = true;
                    break;
                case 302:
                    alert('해당 아이디는 사용 불가능합니다ㅠㅠ');
                    break;
            }
        })
}