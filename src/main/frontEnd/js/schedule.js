window.addEventListener("DOMContentLoaded", function() {
    const termSpan = document.getElementById("curr-sched-term-name");

    const getTermURL = `http://localhost:8080/term-test`;
    fetch(getTermURL)
        .then(data => {
            data.json().then((data) => {
                termSpan.innerText = data.name;
            });
        });
});


window.addEventListener("DOMContentLoaded",function() {
    updateSchedule()
});

function updateSchedule(){
    getCurrentSchedule()
}

function getCurrentSchedule(){
    
    const getScheduleURL = `http://localhost:8080/calendar?id=` + 1;

    const container = document.getElementById("schedule-classes-list");


    fetch(getScheduleURL)
        .then(data => {
        data.json().then((data) => {
            if (data.length === 0) {
                container.innerHTML = "<p>Schedule empty</p>";
            } else {
                for (const c of data) {
                    const p = coFactory.createClassObject(c)
                    container.append(p);
                }
            }

        });
    });
}