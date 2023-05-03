import { setCookie, getCookie } from './useCookies.js';

window.addEventListener("DOMContentLoaded", function() {
    const termSpan = document.getElementById("curr-sched-term-name");

    const getTermURL = `http://localhost:8080/term-test`;
    fetch(getTermURL)
        .then(data => {
            data.json().then((data) => {
                termSpan.innerText = data.name;
            });
        });






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
      const queryString = `tr:nth-child(${getRowIndex(cls.startTime)}) td:nth-child(${getColumnIndex(cls.dayOfWeek)})`;
      console.log(queryString);
      const cell = table.querySelector(queryString);
      if(!cell){console.log("ah shit, it's null!")}
      cell.innerText = cls.name;
    });
});


window.addEventListener("DOMContentLoaded",function() {
    updateSchedule()
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
  console.log(columns.indexOf(dayOfWeek) + 2);
  return columns.indexOf(dayOfWeek) + 2;
}
function getRowIndex(classTime) {
  const rows = ['8:00 AM','8:30 AM', '9:00 AM', '9:30 AM', '10:00 AM', '10:30 AM', '11:00 AM', '11:30 AM', '12:00 PM', '12:30 PM','1:00 PM','1:30 PM','2:00 PM','2:30 PM','3:00 PM','3:30 PM','4:00 PM','4:30 PM','5:00 PM','5:30 PM','6:00 PM','6:30 PM','7:00 PM','7:30 PM',];
  console.log(rows.indexOf(classTime) + 2);
  return rows.indexOf(classTime) + 2;
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
        // if()

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

    fetch(getAllUserSchedulesURL)
        .then(data => {
        data.json().then((data) => {
            if (data.length === 0) {
                const sch = document.createElement("p");
                sch.innerText = "No schedules";
                container.prepend(sch)
            } else {
                for (const s of data) {
                    insertSchedule(s, template, container);
                }
            }

        });
    });
}

function insertSchedule(s, template, container) {
    const clonedElement = template.cloneNode(true);

    clonedElement.id = "sched-" + s[0];
    clonedElement.firstChild.innerText = s[1];
    clonedElement.childNodes[2].innerHTML = s[2];
    clonedElement.classList.add("sched-in-list");

    container.prepend(clonedElement);
}






function updateSchedule(){
    const container = document.getElementById("schedule-classes-list");
    const scheduleHeader = document.getElementById("schedule-display-header");
    container.innerText = 'Classes';
    container.innerHTML = '';
    container.append(scheduleHeader);
    getCurrentSchedule();
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