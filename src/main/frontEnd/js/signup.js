import { setCookie, getCookie } from './useCookies.js';

window.addEventListener("DOMContentLoaded", function() {

    const submitBtn = document.getElementById("submit-btn");
    submitBtn.addEventListener('click', function() {
        
        // event.preventDefault(); // prevent the form from submitting
        const inName = document.getElementById('name').value;
        const inEmail = document.getElementById('email').value;
        const inPassword = document.getElementById('p1').value;
        const inConfirm = document.getElementById('p2').value;

        console.log(inName);
        console.log(inEmail);
        console.log(inPassword);
        console.log(inConfirm);

        if(inPassword===inConfirm) {
            signup(inName, inEmail, inPassword)
        } else {
            console.log("Passwords don't match");
        }

    })

});

async function signup(n, e, p) {
    console.log(n);
    console.log(e);
    console.log(p);
    const url = 'http://localhost:8080/signup';
    const data = {email: e, password: p};
    const options = {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    };


    fetch(url, options)
        .then(data => {
            data.json().then((data) => {
                const bool = data;
                console.log("bool: " + data);
                if (bool === 1) {
                    console.log("Succesful sign up");
                    window.location.href = "/login.html";
                } else {
                    console.log("Could not create account");
                }
            });
        });
}


