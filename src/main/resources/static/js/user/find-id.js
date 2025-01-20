const emailInput = document.querySelector('.email-cert-container input')
const confirmButton = document.querySelector('button');
const findIdDiv = document.querySelector('.find-id-container');
const errorDiv = document.querySelector('.error-container');

confirmButton.onclick = () => {
    findIdDiv.setAttribute('active','');
    const findIdSpan = findIdDiv.querySelector('label span');
    fetch(`/user/findId/${emailInput.value}`)
        .then(response => {
            console.log(response)
            switch(response.status){
                case 302:
                    return response.text();
                default:
                    errorDiv.setAttribute('active', '');
                    return;
            }
        })
        .then(value => {
            console.log(value);
            findIdSpan.textContent = `당신의 아이디는 ${value} 입니다`;
        })
}