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
  const cell = table.querySelector('tr:nth-child(${getRowIndex(cls.startTime)}) tr:nth-child(${getColumnIndex(cls.dayOfWeek)})');
  cell.innerText = "hello?";
});

// helper function to get the column index of a day of the week
function getColumnIndex(dayOfWeek) {
  const columns = ['Sunday','Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
  return columns.indexOf(dayOfWeek) + 1;
}
function getRowIndex(classTime) {
  const rows = ['8:00 AM','8:30 AM', '9:00 AM', '9:30 AM', '10:00 AM', '10:30 AM', '11:00 AM', '11:30 AM', '12:00 PM', '12:30 PM','1:00 PM','1:30 PM','2:00 PM','2:30 PM','3:00 PM','3:30 PM','4:00 PM','4:30 PM','5:00 PM','5:30 PM','6:00 PM','6:30 PM','7:00 PM','7:30 PM',];
  return columns.indexOf(classTime) + 1;
}


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