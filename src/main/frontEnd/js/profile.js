import { setCookie, getCookie } from './useCookies.js';

window.addEventListener("DOMContentLoaded", function() {


    const profileBtn = this.document.getElementById("login-button");
    const userID = getCookie("user");

    if (userID !== "") {
        profileBtn.innerText="Log out";
        profileBtn.addEventListener("click", function() {
            const logoutURL = "http://localhost:8080/logout?loginSecret=" + userID;
            fetch(logoutURL)
            .then(data => {
                data.json().then((data) => {
                    if (data === 1) {
                        setCookie("user", "", 100);
                        location.reload();
                    } else {
                        console.log("didn't log out");
                    }

                });
            });
        });
    } else {
        profileBtn.innerText="Login";
        profileBtn.addEventListener("click", function() {
            window.location.href='/login.html';
        });
    }

})