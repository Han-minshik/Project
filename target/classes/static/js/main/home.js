// $(document).ready(function () {
//
//     mobileSize();
//
//     function mobileSize() {
//         var realSize = window.innerWidth;
//         $("body").css("width", realSize);
//         console.log("디바이스 실사이즈:" + realSize);
//         console.log("모바일 실사이즈:" + $("body").css("width"));
//     }
//
// });

// let rollUpBtn = document.querySelector('#rollUp');
//
// const scroll = () => {
//     if (window.scrollY !== 0) {
//         setTimeout(() => {
//             window.scrollTo(0, window.scrollY - 50);
//             scroll();
//         }, 10);
//     }
// }
//
// rollUpBtn.addEventListener('click', scroll);
//


document.addEventListener("DOMContentLoaded", function () {
    mobileSize();

    function mobileSize() {
        var realSize = window.innerWidth;
        document.body.style.width = realSize + "px";
        console.log("디바이스 실사이즈: " + realSize);
        console.log("모바일 실사이즈: " + document.body.style.width);
    }

    let rollUpBtn = document.querySelector('#rollUp');

    const scroll = () => {
        if (window.scrollY !== 0) {
            setTimeout(() => {
                window.scrollTo(0, window.scrollY - 50);
                scroll();
            }, 10);
        }
    };

    rollUpBtn.addEventListener("click", scroll);
});

function includeHTML(callback) {
    var z, i, elmnt, file, xhr;
    /*loop through a collection of all HTML elements:*/
    z = document.getElementsByTagName("*");
    for (i = 0; i < z.length; i++) {
        elmnt = z[i];
        /*search for elements with a certain atrribute:*/
        file = elmnt.getAttribute("include-html");
        //console.log(file);
        if (file) {
            /*make an HTTP request using the attribute value as the file name:*/
            xhr = new XMLHttpRequest();
            xhr.onreadystatechange = function() {
                if (this.readyState == 4) {
                    if (this.status == 200) {
                        elmnt.innerHTML = this.responseText;
                    }
                    if (this.status == 404) {
                        elmnt.innerHTML = "Page not found.";
                    }
                    /*remove the attribute, and call this function once more:*/
                    elmnt.removeAttribute("include-html");
                    includeHTML(callback);
                }
            };
            xhr.open("GET", file, true);
            xhr.send();
            /*exit the function:*/
            return;
        }
    }
    setTimeout(function() {
        callback();
    }, 0);
}

    // header와 footer를 불러오는 함수
    function loadHTML(elementId, file) {
    fetch(file)
        .then(response => response.text())
        .then(html => {
            document.getElementById(elementId).innerHTML = html;
        })
        .catch(err => console.warn('HTML 로딩 오류:', err));
}

    // 페이지 로드 시 header와 footer를 불러오기
    window.onload = function() {
    loadHTML('header-container', '/header');  // 서버에서 반환된 HTML 삽입
    loadHTML('footer-container', '/footer');  // 서버에서 반환된 HTML 삽입
};
