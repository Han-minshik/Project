const changePwButton = document.querySelector('#pw-change-input-section button')
changePwButton.onclick = () => {
    const csrfToken = document.forms[0].querySelector('input[name=_csrf]').value;
    const password = document.getElementById('password-input')
    console.log(password)
    fetch(`/changePw/password`,{
        method: "POST",
        headers:{
            "X-CSRF-TOKEN": csrfToken,
            "Content-Type": "text/plain"
        },
        body: password
    }).then(response => {
        if(response.ok){
            alert('비밀번호 변경 완료')
            location.href='/user/login'
        }else{
            alert('비밀번호 변경 실패')
        }
    })
}