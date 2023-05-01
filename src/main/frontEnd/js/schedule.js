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
    const container = document.getElementById("schedule-classes-list");
    const scheduleHeader = document.getElementById("schedule-display-header");
    container.innerText = 'Classes'
    container.innerHTML = ''
    container.append(scheduleHeader)
    getCurrentSchedule()
}

function getCurrentSchedule(){
    
    const getScheduleURL = `http://localhost:8080/calendar?id=` + 1;

    const container = document.getElementById("schedule-classes-list");


    fetch(getScheduleURL)
        .then(data => {
        data.json().then((data) => {
            if (data.length === 0) {
                const sch = document.createElement("p");
                sch.innerText = "Schedule empty";
                container.append(sch)
            } else {
                for (const c of data) {
                    const p = coFactory.createClassObject(c)
                    container.append(p);
                }
            }

        });
    });
}

function addClassToSchedule(courseCode){

    var currentSchedule = 1;
    var ccSplit = courseCode.split(" ")
    console.log(courseCode)
    console.log(ccSplit)

    const addClassURL = `http://localhost:8080/addClass?` +
        `scheduleID=` + currentSchedule +
        `&dept=` + ccSplit[0] + 
        `&courseNum=` + ccSplit[1] + 
        `&section=` + ccSplit[2] +
        `&year=2020` + 
        `&term=30`;
    console.log(addClassURL)


    fetch(addClassURL)
        .then(data => {
        data.json().then((data) => {
            updateSchedule()
        });
    });
}

function removeClassFromSchedule(courseCode){

    var currentSchedule = 1;
    var ccSplit = courseCode.split(" ")
    console.log(courseCode)
    console.log(ccSplit)

    const addClassURL = `http://localhost:8080/removeClass?` +
        `scheduleID=` + currentSchedule +
        `&dept=` + ccSplit[0] + 
        `&courseNum=` + ccSplit[1] + 
        `&section=` + ccSplit[2] + 
        `&year=2020` + 
        `&term=30`;
    console.log(addClassURL)

    fetch(addClassURL)
        .then(data => {
        data.json().then((data) => {
            updateSchedule()
        });
    });
};