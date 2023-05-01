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
                    const id = data.id;
                    if (id !== -1) {
                        setCookie("user", id, 30);
                    }
                });
            });
    } catch (error) {
        return console.error(error);
    }
}


function setCookie(cname, cvalue, exdays) {
    const d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    let expires = "expires="+ d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}

function getCookie(cname) {
    let name = cname + "=";
    let decodedCookie = decodeURIComponent(document.cookie);
    let ca = decodedCookie.split(';');
    for(let i = 0; i <ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) == ' ') {
        c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
        return c.substring(name.length, c.length);
        }
    }
    return "";
}