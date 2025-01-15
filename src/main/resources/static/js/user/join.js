const idInput = document.getElementById('id-input');
const nameInput = document.getElementById('name-input');
const [idDuplicateButton, nameDuplicateButton] = document.querySelectorAll('.duplicate-check');

const telAuthButton = document.querySelector('.tel-input-container button');

const [emailLocal, emailDomain] = document.getElementsByName('email');
const emailAuthButton = document.querySelector('#email-input-container button')
const emailAuthConfirmInput = document.querySelector('#email-auth-input-container input')
const emailAuthConfirmButton = document.querySelector('#email-auth-input-container button')


const cancelButton = document.getElementById('cancel');

let telAuthCompleted = false;
let emailAuthCompleted = false;

IMP.init("imp30720372"); // 본인 고유 imp

/************************************************/
function checkDuplicate(inputElement) {
    const name = inputElement.name;
    const value = inputElement.value.trim();

    console.log("n,v : ", name, value);
    if (value === '') {
        alert(`${name} 입력하쇼`);
        return;
    }
    fetch(`/user/${name}/${value}`)
        .then(response => {
            console.log(response);
            switch (response.status) {
                case 200:
                    alert(`${name} 사용 가능`);
                    break;
                default:
                    alert(`중복 ${name} 사용 불가능`);
                    break;
            }
        })
        .catch(error => console.error('Error:', error));
}

idDuplicateButton.onclick = () => {
    checkDuplicate(idInput);
};

nameDuplicateButton.onclick = () => {
    checkDuplicate(nameInput);
};
/***********************************************************/

telAuthButton.onclick = () => {
    IMP.certification(
        {
            channelKey: "channel-key-7a2760a1-e367-48b3-870f-c1b6f2e28776", // KG이니시스
            merchant_uid: "ORD20180131-0000011",
        },
        function (importResponse){
            if(importResponse.success){
                const csrfToken = document.forms[0].querySelector('input[name=_csrf]').value;
                const impUid = importResponse['imp_uid'];
                fetch(`/tel/auth`, {
                    method: "POST",
                    headers: {"X-CSRF-TOKEN": csrfToken},
                    body: impUid
                }).then(response => {
                    if(response.ok){
                        alert('본인인증 완료')
                    }
                })
                telAuthCompleted=true;
            }else{
                alert('본인인증 실패')
                telAuthCompleted=false;
            }
        }
    )
}

/***********************************************************/
emailAuthButton.onclick = () => {
    const csrfToken = document.forms[0].querySelector('input[name=_csrf]').value;
    const email = `${emailLocal.value}@${emailDomain.value}`;
    console.log(email)
    fetch(`/email/auth`,{
        method: "POST",
        headers:{
            "X-CSRF-TOKEN": csrfToken,
            "Content-Type": "text/plain"
        },
        body: email
    }).then(response => {
        if(response.ok){
            alert('인증번호 전송 완료')
        }else{
            alert('인증번호 전송 실패')
        }
    })
}

emailAuthConfirmButton.onclick = () => {
    const email = `${emailLocal.value}@${emailDomain.value}`;
    const certNumber = emailAuthConfirmInput.value;
    fetch(`/email/auth?email=${email}&certNumber=${certNumber}`)
        .then(response => {
            if(response.ok){
                alert('이메일 인증 완료')
                emailAuthCompleted = true;
                emailAuthConfirmInput.setAttribute('disabled', '');
                emailAuthConfirmButton.setAttribute('disabled', '');
            }else{
                alert('이메일 인증 실패')
                emailAuthCompleted = false;
            }
        })
}

// document.forms[0].onsubmit = event => {
//     event.preventDefault();
//     alert("가입완료")
// }

cancelButton.onclick = () => {
    location.href="/";
}
