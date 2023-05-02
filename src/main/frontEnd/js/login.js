import { setCookie, getCookie } from './useCookies.js';

window.addEventListener("DOMContentLoaded", function() {


    const form = document.getElementById("login-form");
    form.addEventListener('submit', (event) => {
        event.preventDefault(); // prevent the form from submitting
        const formData = new FormData(form);
        const inEmail = formData.get('email');
        const inPassword = formData.get('password');

        login(inEmail, inPassword)
            .catch(error => console.error(error));
        })

});

async function login(e, p) {
    const url = 'http://localhost:8080/login';
    const data = {email: e, password: p};
    const options = {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    };

    try {
        fetch(url, options)
            .then(data => {
                data.json().then((data) => {
                    const id = data;
                    console.log("ID: " + data);
                    if (id !== -1) {
                        setCookie("user", id, 30);
                        // console.log(getCookie("user"));
                        window.location.href = "/home.html";
                    }
                });
            });
    } catch (error) {
        return console.error(error);
    }
}


