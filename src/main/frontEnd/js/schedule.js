import { setCookie, getCookie } from './useCookies.js';
import { coFactory } from './courseInfo.js';

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

window.addEventListener("DOMContentLoaded",function() {
    getMyScheduleNames();

    const newScBtn = document.getElementById("new-sched-button");
    const newScForm = document.getElementsByClassName("new-sc-form")[0];
    const newScSubmit = this.document.getElementById("new-sc-submit");

    newScBtn.addEventListener("click", function() {
        newScForm.classList.toggle("sc-form-active");
    });

    newScSubmit.addEventListener("click", function() {
        createNewSchedule();
    });
});


function createNewSchedule() {
    const name = document.getElementById("new-sc-name").value;
    const term = document.getElementById("new-sc-term").value;

    const url = 'http://localhost:8080/makeNewSchedule';
    //TODO: make this secure
    const data = {loginSecret: getCookie("user"), name: name, term: term};
    const options = {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    };

    if (name !== "") {
        fetch(url, options)
            .then(data => {
                data.json().then((data) => {
                    const tmp = document.getElementById("hidden-sched-name-temp");
                    const cont = document.getElementById("sc-list-id");
                    insertSchedule(data, tmp, cont);
                });
            });
        document.getElementsByClassName("new-sc-form")[0].classList.toggle("sc-form-active");
    }
}


function getMyScheduleNames(){
    //TODO: needs to actually check that I'm signed in!!
    const userSecret = getCookie("user");
    const getAllUserSchedulesURL = `http://localhost:8080/getMySchedules?loginSecret=` + userSecret;
    const container = document.getElementById("sc-list-id");
    const template = document.getElementById("hidden-sched-name-temp");

    // fetch(getAllUserSchedulesURL)
    //     .then(data => {
    //     data.json().then((data) => {
    //         if (data.length === 0) {
    //             const sch = document.createElement("p");
    //             sch.innerText = "No schedules";
    //             container.prepend(sch)
    //         } else {
    //             for (const s of data) {
    //                 insertSchedule(s, template, container);
    //             }
    //         }

    //     });
    // });
}

function insertSchedule(s, template, container) {
    const clonedElement = template.cloneNode(true);

    clonedElement.id = "sched-" + s[0];
    clonedElement.firstChild.innerText = s[1];
    clonedElement.childNodes[2].innerHTML = s[2];
    clonedElement.classList.add("sched-in-list");

    container.prepend(clonedElement);
}

async function getCurrentSchedule(scheduleNum){
    
    const getScheduleURL = 'http://localhost:8080/getSchedule?id=' + scheduleNum;

    const container = document.getElementById("schedule-classes-list");

    const data = await fetch(getScheduleURL)
    const dataJson = await data.json()
    return dataJson

}

async function updateSchedule(){
    const container = document.getElementById("schedule-classes-list");
    const scheduleHeader = document.getElementById("schedule-display-header");
    const scheduleTerm = document.getElementById("curr-sched-term-name")

    scheduleHeader.innerText = 'Loading Classes'
    scheduleTerm.innerText = ''
    container.innerText = ''
    container.innerHTML = ''

    const schedule = await getCurrentSchedule(1);
    container.append(scheduleHeader)
    updateClassDisplayList(schedule, container, scheduleHeader, scheduleTerm)
}

function updateClassDisplayList(schedule, cont, Header){

    // console.log(schedule);

    if (schedule === null){
        const sch = document.createElement("p");
        sch.innerText = "Schedule empty";
        cont.append(sch)
        return;
    }
    for(const c of schedule.classes){
        const p = coFactory.createClassObjectFromJSON(c, true)
        cont.append(p);
    }

    const termHeader = document.createElement("span")
    termHeader.id = "curr-sched-term-name"
    termHeader.innerText = schedule.term.name

    Header.innerText = schedule.name + ": "
    Header.append(termHeader)
}

export async function addClassToSchedule(courseCode){

    var currentSchedule = 1;
    var ccSplit = courseCode.split(" ")
    // console.log(courseCode)

    const addClassURL = 'http://localhost:8080/addClass?' +
        'scheduleID=' + currentSchedule +
        '&dept=' + ccSplit[2] + 
        '&courseNum=' + ccSplit[3] + 
        '&section=' + ccSplit[4] +
        '&year=2020' + 
        '&term=30';

    console.log(addClassURL)

    // const data = {scheduleID: currentSchedule, courseCode: courseCode};
    // const options = {
    //     method: 'POST',
    //     headers: {'Content-Type': 'application/json'},
    //     body: JSON.stringify(data)
    // };
    // const addClassURL = 'http://localhost:8080/addClassTest';

    const data = await fetch(addClassURL)
    const dataJSON = await data.json()
    // console.log(dataJSON)
    if(dataJSON.Succeeded[0] == "True"){
        updateSchedule()
        console.log("Successfully added class")
    }
    else{
        console.log("Failed to add class to schedule")
        console.log(dataJSON.ErrorMessage[0])
    }
}

export async function removeClassFromSchedule(courseCode){

    var currentSchedule = 1;
    var ccSplit = courseCode.split(" ")
    // console.log(courseCode)
    // console.log(ccSplit)

    const removeClassURL = 'http://localhost:8080/removeClass?' +
        'scheduleID=' + currentSchedule +
        '&dept=' + ccSplit[2] + 
        '&courseNum=' + ccSplit[3] + 
        '&section=' + ccSplit[4] + 
        '&year=2020' + 
        '&term=30';
    console.log(removeClassURL)

    const data = await fetch(removeClassURL)
    const dataJSON = await data.json()
    // console.log(dataJSON)
    if(dataJSON.Succeeded[0] == "True"){
        updateSchedule()
        console.log("Successfully removed class")
    }
    else{
        console.log("Failed to remove class to schedule")
        console.log(dataJSON.ErrorMessage[0])
    }
};