import { setCookie, getCookie } from './useCookies.js';
import { coFactory } from './courseInfo.js';


// No longer need this test:
// window.addEventListener("DOMContentLoaded", function() {
//     const termSpan = document.getElementById("curr-sched-term-name");

//     const getTermURL = 'http://localhost:8080/term-test';
//     fetch(getTermURL)
//         .then(data => {
//             data.json().then((data) => {
//                 termSpan.innerText = data.name;
//             });
//         });
// });


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
        location.reload();
    });
});

function setUserCurrScheduleInCookie(userSecret, sched) {
    const userCurrentScheduleCookieKey = "user" + userSecret + "currSched";
    setCookie(userCurrentScheduleCookieKey, sched, 10);

    console.log("curr sched is: " + getUserCurrScheduleFromCookie(userSecret));
}

function getUserCurrScheduleFromCookie(userSecret) {
    const userCurrentScheduleCookieKey = "user" + userSecret + "currSched";
    return getCookie(userCurrentScheduleCookieKey);
}

function createNewSchedule() {
    const name = document.getElementById("new-sc-name").value;
    const term = document.getElementById("new-sc-term").value;

    const userSecret = getCookie("user");

    const url = 'http://localhost:8080/makeNewSchedule';
    //TODO: make this secure
    const data = {loginSecret: userSecret, name: name, term: term};
    const options = {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    };


    if (name !== "") {
        fetch(url, options)
            .then(data => {
                data.json().then((data) => {

                    //move to after
                    console.log("create and set new sched: " + data[0]);
                    setUserCurrScheduleInCookie(userSecret, data[0]);


                    const tmp = document.getElementById("hidden-sched-name-temp");
                    const cont = document.getElementById("sc-list-id");
                    insertSchedule(data, tmp, cont, userSecret);

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

    fetch(getAllUserSchedulesURL)
        .then(data => {
        data.json().then((data) => {
            if (data.length === 0) {
                const sch = document.createElement("p");
                sch.innerText = "No schedules";
                container.prepend(sch)
            } else {
                //set latest schedule as current schedule, if there is no cur schedule set
                //TODO: i don't think this fires when it should

                console.log(document.cookie);

                if (getUserCurrScheduleFromCookie(userSecret) === "") {
                    console.log("running over: " + getUserCurrScheduleFromCookie(userSecret));
                    setUserCurrScheduleInCookie(userSecret, data[data.length-1][0]);
                }

                for (const s of data) {
                    insertSchedule(s, template, container, userSecret);
                }
            }

        });
    });
}

function insertSchedule(s, template, container, userSecret) {
    const clonedElement = template.cloneNode(true);

    clonedElement.id = "sched-" + s[0];
    clonedElement.firstChild.innerText = s[1];
    clonedElement.childNodes[2].innerHTML = s[2];
    clonedElement.classList.add("sched-in-list");

    clonedElement.addEventListener("click", function() {
        if (userSecret === "") {
            console.log("USER IS UNDEFINED SOMEHOW, GET SET CURRENT SCHEDULE CLICK EVENT PROPERLY");
        } else {
            setUserCurrScheduleInCookie(userSecret, s[0]);
            console.log("set new cur: " + s[0]);
            location.reload();
        }
    });

    container.prepend(clonedElement);
}


async function getCurrentSchedule(scheduleNum, loginSecret){
    const getScheduleURL = 'http://localhost:8080/getSchedule?id=' + scheduleNum + "&loginSecret=" + loginSecret;

    const container = document.getElementById("schedule-classes-list");

    const data = await fetch(getScheduleURL)

    console.log(data);

    const dataJson = await data.json()


    console.log(dataJson);


    return dataJson
}


async function updateSchedule(){
    const curUserSecret = getCookie("user");
    if (curUserSecret !== ""){
        const container = document.getElementById("schedule-classes-list");
        const scheduleHeader = document.getElementById("schedule-display-header");
        const scheduleTerm = document.getElementById("curr-sched-term-name")

        scheduleHeader.innerText = 'Loading Classes...'
        scheduleTerm.innerText = ''
        container.innerText = ''
        container.innerHTML = ''

        const schedule = await getCurrentSchedule(getUserCurrScheduleFromCookie(curUserSecret), curUserSecret);
        container.append(scheduleHeader)
        updateClassDisplayList(schedule, container, scheduleHeader, scheduleTerm)
    } else {
        console.log("NO ACCESS TO CURRENT SCHEDULE, YOU'RE NOT LOGGED IN");
    }

}

function updateClassDisplayList(schedule, cont, Header){

    console.log(schedule);

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

export function addClassToSchedule(courseCode){

    var currentSchedule = 1;
    var ccSplit = courseCode.split(" ")
    console.log(courseCode)
    // console.log(ccSplit)

    const addClassURL = 'http://localhost:8080/addClass?' +
        'scheduleID=' + currentSchedule +
        '&dept=' + ccSplit[2] + 
        '&courseNum=' + ccSplit[3] + 
        '&section=' + ccSplit[4] +
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

export function removeClassFromSchedule(courseCode){

    var currentSchedule = 1;
    var ccSplit = courseCode.split(" ")
    console.log(courseCode)
    console.log(ccSplit)

    const addClassURL = 'http://localhost:8080/removeClass?' +
        'scheduleID=' + currentSchedule +
        '&dept=' + ccSplit[2] + 
        '&courseNum=' + ccSplit[3] + 
        '&section=' + ccSplit[4] + 
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