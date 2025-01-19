$(document).ready(function () {

    mobileSize();

    function mobileSize() {
        var realSize = window.innerWidth;
        $("body").css("width", realSize);
        console.log("디바이스 실사이즈:" + realSize);
        console.log("모바일 실사이즈:" + $("body").css("width"));
    }

});

let rollUpBtn = document.querySelector('#rollUp');

const scroll = () => {
    if (window.scrollY !== 0) {
        setTimeout(() => {
            window.scrollTo(0, window.scrollY - 50);
            scroll();
        }, 10);
    }
}

rollUpBtn.addEventListener('click', scroll);




