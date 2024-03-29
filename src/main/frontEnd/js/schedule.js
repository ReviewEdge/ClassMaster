import { setCookie, getCookie } from './useCookies.js';
import { coFactory } from './courseInfo.js';
import { refreshCalendar } from './cal.js';


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

function testClassCalendar(){
// create an array of Class objects
    const classes = [
        new Class('Math 101', '8:00 AM', '8:30 AM', 'Sunday'),
        new Class('Physics 201', '9:00 AM', '10:30 AM', 'Tuesday'),
        new Class('English 202', '11:00 AM', '12:30 PM', 'Monday'),
        // more Class objects
        ];

        // loop through each Class object and add it to the table cell that corresponds to its day and time
    const table = document.getElementById('calendar-view-table');
    classes.forEach(cls => {
        // const cell = table.querySelector('tr:nth-child(${getRowIndex(cls.startTime)}) td:nth-child(${getColumnIndex(cls.dayOfWeek)})');
        // cell.innerText = "hello?";
    });  
}



window.addEventListener("DOMContentLoaded",function() {
    updateSchedule()
    testClassCalendar()
});

/* AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
                    This is new stuff           **/

// define your Java objects (for example, a Class object)
class Class {
    constructor(name, startTime, endTime, dayOfWeek) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayOfWeek = dayOfWeek;
    }
}

  
  
// helper function to get the column index of a day of the week
function getColumnIndex(dayOfWeek) {
const columns = ['Sunday','Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
return columns.indexOf(dayOfWeek) + 1;
}
function getRowIndex(classTime) {
const rows = ['8:00 AM','8:30 AM', '9:00 AM', '9:30 AM', '10:00 AM', '10:30 AM', '11:00 AM', '11:30 AM', '12:00 PM', '12:30 PM','1:00 PM','1:30 PM','2:00 PM','2:30 PM','3:00 PM','3:30 PM','4:00 PM','4:30 PM','5:00 PM','5:30 PM','6:00 PM','6:30 PM','7:00 PM','7:30 PM',];
return columns.indexOf(classTime) + 1;
}
  
  

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
                //TODO: i don't think this fires when it should?

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


export async function getCurrentSchedule(loginSecret){

    const scheduleNum = getUserCurrScheduleFromCookie(loginSecret);

    const getScheduleURL = 'http://localhost:8080/getSchedule?id=' + scheduleNum + "&loginSecret=" + loginSecret;

    const container = document.getElementById("schedule-classes-list");

    const data = await fetch(getScheduleURL)

    // console.log(data);

    const dataJson = await data.json()


    // console.log(dataJson);


    return dataJson
}


async function updateSchedule(){
    const curUserSecret = getCookie("user");
    if ((curUserSecret !== "") && (getUserCurrScheduleFromCookie(curUserSecret))) {
        const container = document.getElementById("schedule-classes-list");
        const scheduleHeader = document.getElementById("schedule-display-header");
        const scheduleTerm = document.getElementById("curr-sched-term-name")

        scheduleHeader.innerText = 'Loading Classes...'
        scheduleTerm.innerText = ''
        container.innerText = ''
        container.innerHTML = ''

        const schedule = await getCurrentSchedule(curUserSecret);
        container.append(scheduleHeader)
        updateClassDisplayList(schedule, container, scheduleHeader, scheduleTerm)
        refreshCalendar(schedule)
    } else {
        if (getUserCurrScheduleFromCookie(curUserSecret)) {
            alert("Please login to save or add to a schedule")
            console.log("NO ACCESS TO CURRENT SCHEDULE, YOU'RE NOT LOGGED IN");
        }
    }
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

    const hamMen = document.getElementsByClassName('hamburger-menu')[0];
    const header = document.getElementsByClassName('header')[0];
    // change to season theme color
    if (schedule.term.name === "Fall 2020") {
        hamMen.classList.add("fall");
        header.classList.add("fall");
    } else if (schedule.term.name === "Spring 2021") {
        hamMen.classList.add("spring");
        header.classList.add("spring");
    }

    const termHeader = document.createElement("span")
    termHeader.id = "curr-sched-term-name"
    termHeader.innerText = schedule.term.name

    Header.innerText = schedule.name + ": "
    Header.append(termHeader)
}

export async function addClassToSchedule(courseCode){

    //TODO: Should check if cl and sc terms match in the FE and tell user if they don't

    const userSecret = getCookie("user");

    if(userSecret === "") {
        alert("Please login to add to a schedule")
        console.log("CAN'T ADD A CLASS, NO USER LOGGED IN");
    } else {
        var currentSchedule = getUserCurrScheduleFromCookie(userSecret);
        var ccSplit = courseCode.split(" ")
    
        const addClassURL = 'http://localhost:8080/addClass?' +
            'loginSecret=' + userSecret +
            '&scheduleID=' + currentSchedule +
            '&dept=' + ccSplit[2] + 
            '&courseNum=' + ccSplit[3] + 
            '&section=' + ccSplit[4] +
            '&year=' + ccSplit[0] + 
            '&term=' + ccSplit[1];
    
        // const data = {scheduleID: currentSchedule, courseCode: courseCode};
        // const options = {
        //     method: 'POST',
        //     headers: {'Content-Type': 'application/json'},
        //     body: JSON.stringify(data)
        // };
        // const addClassURL = 'http://localhost:8080/addClassTest';
    
        console.log(addClassURL)
        // console.log(options)
        
        const data = await fetch(addClassURL)
        const dataJSON = await data.json()
        // console.log(dataJSON)
        if(dataJSON.Succeeded[0] == "True"){
            updateSchedule()
            console.log("Successfully added class")
        }
        else{
            alert("Failed to add to a schedule:\n" + dataJSON.ErrorMessage[0])
            console.log("Failed to add class to schedule")
            console.log(dataJSON.ErrorMessage[0])
        }
    }

}

export async function removeClassFromSchedule(courseCode){

    const userSecret = getCookie("user");

    if(userSecret === "") {
        alert("Please login to remove from a schedule")
        console.log("CAN'T REMOVE A CLASS, NO USER LOGGED IN");
    } else {
        var currentSchedule = getUserCurrScheduleFromCookie(userSecret);
        var ccSplit = courseCode.split(" ")
        // console.log(courseCode)
        // console.log(ccSplit)

        const removeClassURL = 'http://localhost:8080/removeClass?' +
            'loginSecret=' + userSecret +
            '&scheduleID=' + currentSchedule +
            '&dept=' + ccSplit[2] + 
            '&courseNum=' + ccSplit[3] + 
            '&section=' + ccSplit[4] + 
            '&year=' + ccSplit[0] + 
            '&term=' + ccSplit[1];

        console.log(removeClassURL)

        const data = await fetch(removeClassURL)
        const dataJSON = await data.json()
        // console.log(dataJSON)
        if(dataJSON.Succeeded[0] == "True"){
            updateSchedule()
            console.log("Successfully removed class")
        }
        else{
            alert("Failed to remove to a schedule:\n" + dataJSON.ErrorMessage[0])
            console.log("Failed to remove class to schedule")
            console.log(dataJSON.ErrorMessage[0])
        }
    }
};
