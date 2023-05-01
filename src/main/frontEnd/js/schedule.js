window.addEventListener("DOMContentLoaded", function() {
    const termSpan = document.getElementById("curr-sched-term-name");
    const schedName = document.getElementById("curr-sched-name");
    const courseList = document.getElementById("course-list");
    const getTermURL = `http://localhost:8080/term-test`;
    fetch(getTermURL)
        .then(data => {
            data.json().then((data) => {
                termSpan.innerText = data.name;
            });
        });
        fetch('http://localhost:8080/schedule-name')
            .then(response => {
            console.log(response);
            response.json())
            }}

              .then(data => {
                schedName.innerText = data.name;
              })
               .catch(error => {
               console.log(error);
                    schedName.innerText = "yikes";
                });
});
window.addEventListener("DOMContentLoaded", updateSchedule());


function updateSchedule(){
    console.log(getCurrentSchedule())
}

function getCurrentSchedule(){
    const getSearchURL = `http://localhost:8080/schedule?query=` + searchQuery; TODO fix this url
    const container = document.getElementById("schedule-classes-list"); //this is how you pull from html
    let schedule = "";
    fetch(getSearchURL)
    .then(response => { return response.json() })
    .then(data => schedule = data)
}