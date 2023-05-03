window.addEventListener("DOMContentLoaded", function() {
    const termSpan = document.getElementById("curr-sched-term-name");

    const getTermURL = 'http://localhost:8080/term-test';
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
    var schedule = getCurrentSchedule()
    updateClassDisplayList(schedule)
}

function getCurrentSchedule(){
    
    const getScheduleURL = 'http://localhost:8080/getSchedule?id=' + 1;

    const container = document.getElementById("schedule-classes-list");


    fetch(getScheduleURL)
        .then(data => {
        data.json().then((data) => {
            // console.log(data)
            if (data.length === 0) {
                const sch = document.createElement("p");
                sch.innerText = "Schedule empty";
                container.append(sch)
            } else {
                for (const c of data) {
                    const p = coFactory.createClassObject(c, true)
                    container.append(p);
                }
            }

        });
    });
}

function updateClassDisplayList(schedule){
    
}

function addClassToSchedule(courseCode){

    var currentSchedule = 1;
    var ccSplit = courseCode.split(" ")
    // console.log(courseCode)
    // console.log(ccSplit)

    const addClassURL = 'http://localhost:8080/addClass?' +
        'scheduleID=' + currentSchedule +
        '&dept=' + ccSplit[0] + 
        '&courseNum=' + ccSplit[1] + 
        '&section=' + ccSplit[2] +
        '&year=2020' + 
        '&term=30';

    // const data = {scheduleID: currentSchedule, courseCode: courseCode};
    // const options = {
    //     method: 'POST',
    //     headers: {'Content-Type': 'application/json'},
    //     body: JSON.stringify(data)
    // };
    // const addClassURL = 'http://localhost:8080/addClassTest';

    // const addClassURL = 'http://localhost:8080/addClassTest';
    // const data = {email: '1', password: '123'};
    // const options = {
    //     method: 'POST',
    //     headers: {'Content-Type': 'application/json'},
    //     body: JSON.stringify(data)
    // };

    console.log(addClassURL)
    // console.log(options)

    // fetch(addClassURL, options)
    fetch(addClassURL)
        .then(data => {
        data.json().then((data) => {
            console.log(data)
            updateSchedule()
        });
    });
}

function removeClassFromSchedule(courseCode){

    var currentSchedule = 1;
    var ccSplit = courseCode.split(" ")
    console.log(courseCode)
    console.log(ccSplit)

    const addClassURL = 'http://localhost:8080/removeClass?' +
        'scheduleID=' + currentSchedule +
        '&dept=' + ccSplit[0] + 
        '&courseNum=' + ccSplit[1] + 
        '&section=' + ccSplit[2] + 
        '&year=2020' + 
        '&term=30';
    console.log(addClassURL)

    fetch(addClassURL)
        .then(data => {
        data.json().then((data) => {
            updateSchedule()
        });
    });
};