window.addEventListener("DOMContentLoaded", function() {
    const termSpan = document.getElementById("curr-sched-term-name");
    const courseList = document.getElementById("course-list");
    const getTermURL = `http://localhost:8080/term-test`;
    fetch(getTermURL)
        .then(data => {
            data.json().then((data) => {
                termSpan.innerText = data.name;
            });
        });
});
window.addEventListener("DOMContentLoaded", updateSchedule());


function updateSchedule(){
    console.log(getCurrentSchedule())
}

function getCurrentSchedule(){

    const getSearchURL = http://localhost:8080/calendar?id= + 1;
    const container = document.getElementById("schedule-classes-list");

    fetch(getSearchURL)
        .then(data => {
        data.json().then((data) => {
            console.log(data)
            if (data.length === 0) {
                console.log()
                container.innerHTML = "<p>Schedule empty</p>";
            } else {
                container.innerHTML = "My Schedule";
                for (const c of data) {
                    const p = coFactory.createClassObject(c)
                    container.append(p);
                }
            }

        });
    });
}