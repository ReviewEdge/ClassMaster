import { setCookie, getCookie } from './useCookies.js';

window.addEventListener("DOMContentLoaded", function() {
    const termSpan = document.getElementById("curr-sched-term-name");

    const getTermURL = 'http://localhost:8080/term-test';
    fetch(getTermURL)
        .then(data => {
            data.json().then((data) => {
                termSpan.innerText = data.name;
            });
        });


    // loop through each Class object and add it to the table cell that corresponds to its day and time
    const table = document.getElementById('calendar-view-table');

    const getCalendarURL = `http://localhost:8080/calendar`;
   fetch(getCalendarURL)
       .then(response => response.json())
       .then(data => {
           try {
               // Parse the JSON data here
               console.log(data);
           } catch (error) {
               console.error('Error parsing JSON data:', error);
           }
       })
       .catch(error => {
           console.error('Error fetching data:', error);
       });







    const timeslots = [
      new Timeslot('Monday', '12:00 PM', '1:00 PM' ),
      new Timeslot('Wednesday', '12:00 PM', '1:00 PM'),
      new Timeslot('Thursday', '12:30 PM', '1:30 PM'),
      new Timeslot('Friday', '12:00 PM', '1:00 PM')
    ];

    const classes = [new Class('MATH 214 Applied Probabliity and Linear Algebra', timeslots)];

    classes.forEach(cls => {
        cls.timeslots.forEach(slt => {
      const queryString = `tr:nth-child(${getRowIndex(slt.startTime)}) td:nth-child(${getColumnIndex(slt.dayOfWeek)})`;
      console.log(queryString);
      const cell = table.querySelector(queryString);
      if(!cell){console.log("ah shit, it's null!")}
      cell.innerText = cls.name;})
    });
});


window.addEventListener("DOMContentLoaded",function() {
    updateSchedule()
});

// ****defining objects from here****
class Class {
  constructor(name,timeslots) {
    this.name = name;
    this.timeslots = timeslots;
  }
}
class Timeslot {
  constructor(dayOfWeek, startTime, endTime) {
    this.dayOfWeek = dayOfWeek;
    this.startTime = startTime;
    this.endTime = endTime;
  }
}


// helper function to get the column index of a day of the week
function getColumnIndex(dayOfWeek) {
    console.log(dayOfWeek)
  const columns = ['Sunday','Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
  console.log(columns.indexOf(dayOfWeek) + 2);
  return columns.indexOf(dayOfWeek) + 2;
}
function getRowIndex(classTime) {
    console.log(classTime)
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






function updateSchedule(){
    const container = document.getElementById("schedule-classes-list");
    const scheduleHeader = document.getElementById("schedule-display-header");
    container.innerText = 'Classes'
    container.innerHTML = ''
    container.append(scheduleHeader)
    // getCurrentSchedule()
    var schedule = getCurrentSchedule()
    updateClassDisplayList(schedule, container)
}

function getCurrentSchedule(){
    
    const getScheduleURL = 'http://localhost:8080/getCal?id=' + 1;

    const container = document.getElementById("schedule-classes-list");


    fetch(getScheduleURL)
        .then(data => {
        data.json().then((data) => {
            console.log(data)
            return data

            // if (data.length === 0) {
            //     const sch = document.createElement("p");
            //     sch.innerText = "Schedule empty";
            //     container.append(sch)
            // } else {
            //     for (const c of data) {
            //         const p = coFactory.createClassObject(c, true)
            //         container.append(p);
            //     }
            // }

        });
    });
}

function updateClassDisplayList(schedule, container){

    if (schedule === null){
        const sch = document.createElement("p");
        sch.innerText = "Schedule empty";
        container.append(sch)
    }
    for(const c of schedule.classes){
        const p = coFactory.createClassObjectFromJSON(schedule, true)
        container.append(p);
    }
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