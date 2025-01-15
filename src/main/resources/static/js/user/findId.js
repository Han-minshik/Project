const emailInput = document.getElementById('email-input');
const confirmButton = document.querySelector('button');
const showIdDiv = document.getElementById('show-id-container');


confirmButton.onclick = () => {
    showIdDiv.setAttribute('active','');
    const showIdSpan = document.getElementById('show-id-span');
    fetch(`/user/findId/${emailInput.value}`)
        .then(response => {
            console.log(response)
            switch(response.status){
                case 302:
                    return response.text();
                default:
                    break;
            }
        })
        .then(value => {
            console.log(value);
            showIdSpan.textContent = `당신의 아이디는 ${value} 입니다`;
        })
}