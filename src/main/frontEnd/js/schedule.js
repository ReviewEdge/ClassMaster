import { setCookie, getCookie } from './useCookies.js';


// COURSE INFO STUFF
var coFactoryExists = false;
var coFactory_master = null;

class classObjectFactory {

    nextUniqueID;

    constructor(){
        if(coFactoryExists){
            return coFactory_master;
        }
        this.nextUniqueID = 1;
        coFactoryExists = true;
    }

    createClassObject(classInfo, inSchedule) {
        // console.log(classInfo)
        const p = document.createElement("p");
        p.className = 'classText';
        p.id ='classText' + this.nextUniqueID;
    //    p.innerText = ClassInfo + "     (•••)";
        p.innerText = classInfo;
        // p.setAttribute('onclick', 'clickMoreInfo(' + this.nextUniqueID + ')');

        const detailsButton = document.createElement("span");
        // detailsButton.className = 'classText';
        detailsButton.id ='detailsButton' + this.nextUniqueID;
        // detailsButton.innerText = ClassInfo + "     (•••)";
        detailsButton.innerText = "(Click for Details)";
        detailsButton.setAttribute('onclick', 'clickMoreInfo(' + this.nextUniqueID + ')');
        p.append(detailsButton)

        var classInfoSplit = classInfo.split(",");

        if(!(inSchedule)){
            const addButton = document.createElement("span");
            addButton.className = 'addClassButton';
            addButton.id ='addClassButton' + this.nextUniqueID;
            addButton.innerText = "(Click to add Class)";
            // addButton.setAttribute('onclick', 'addClassToSchedule("' + classInfoSplit[0] +'")');
            // addButton.setAttribute("data-courseCode", classInfoSplit[0])
            addButton.addEventListener("click", function() {
                addClassToSchedule(classInfoSplit[0])
            });
            p.append(addButton)
        }
        else {
            const removeButton = document.createElement("span");
            removeButton.className = 'removeClassButton';
            removeButton.id ='removeClassButton' + this.nextUniqueID;
            removeButton.innerText = "(Click to remove Class)";
            // removeButton.setAttribute('onclick', 'removeClassFromSchedule("' + classInfoSplit[0] +'")');
            // removeButton.setAttribute("data-courseCode", classInfoSplit[0])
            removeButton.addEventListener("click", function() {
                removeClassToSchedule(classInfoSplit[0])
            });
            p.append(removeButton)
        }

        const pop = document.createElement("p");
        pop.className = 'classInfoText';
        pop.id ='classInfoText' + this.nextUniqueID;
        pop.innerText = "Get Description Information: WIP";
        p.append(pop);

        this.nextUniqueID++;
        return p;
    }

    createClassObjectFromJSON(classInfoJSON, inSchedule) {
        // console.log(classInfo)
        const p = document.createElement("p");
        p.className = 'classText';
        p.id ='classText' + this.nextUniqueID;
        const courseCodeDiv = classInfoJSON.code.split(" ");
        p.innerText =  courseCodeDiv[2] + " " + courseCodeDiv[3] + ": " + classInfoJSON.title + ", Professor: " + classInfoJSON.professor;
        // p.setAttribute('onclick', 'clickMoreInfo(' + this.nextUniqueID + ')');

        const detailsButton = document.createElement("span");
        // detailsButton.className = 'classText';
        detailsButton.id ='detailsButton' + this.nextUniqueID;
        // detailsButton.innerText = ClassInfo + "     (•••)";
        detailsButton.innerText = "(Click for Details)";
        detailsButton.setAttribute('onclick', 'clickMoreInfo(' + this.nextUniqueID + ')');
        p.append(detailsButton)

        if(!(inSchedule)){
            const addButton = document.createElement("span");
            addButton.className = 'addClassButton';
            addButton.id ='addClassButton' + this.nextUniqueID;
            addButton.innerText = "(Click to add Class)";
            // addButton.setAttribute('onclick', 'addClassToSchedule("' + classInfoJSON.code +'")');
            // addButton.setAttribute("data-courseCode", classInfoJSON.code)
            addButton.addEventListener("click", function() {
                addClassToSchedule(classInfoJSON.code)
            });
            p.append(addButton)
        }
        else {
            const removeButton = document.createElement("span");
            removeButton.className = 'removeClassButton';
            removeButton.id ='removeClassButton' + this.nextUniqueID;
            removeButton.innerText = "(Click to remove Class)";
            // removeButton.setAttribute('onclick', 'removeClassFromSchedule("' + classInfoJSON.code +'")');
            // removeButton.setAttribute("data-courseCode", classInfoJSON.code)
            removeButton.addEventListener("click", function() {
                removeClassToSchedule(classInfoJSON.code)
            });
            p.append(removeButton)
        }

        const pop = document.createElement("p");
        pop.className = 'classInfoText';
        pop.id ='classInfoText' + this.nextUniqueID;
        pop.innerText = "Get Description Information: WIP";
        p.append(pop);
        
        this.nextUniqueID++;
        return p;
    }
}

const coFactory = new classObjectFactory();

function clickMoreInfo(val) {
    var popup = document.getElementById("classInfoText" + val);
    popup.classList.toggle("show");
}














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
    // getCurrentSchedule()

    const schedule = await getCurrentSchedule(1);
    container.append(scheduleHeader)
    updateClassDisplayList(schedule, container, scheduleHeader, scheduleTerm)
    // setAddRemoveButtonFunctionality()
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

// function setAddRemoveButtonFunctionality(){
//     console.log("HIIII")

//     const addButtons = document.getElementsByClassName("addClassButton");
//     for(const b of addButtons){
//         b.addEventListener("click", function() {
//             addClassToSchedule(b.data-courseCode)
//         });
//     }
//     const removeButtons = document.getElementsByClassName("removeClassButton");
//     console.log(removeButtons)
//     for(const b of addButtons){
//         console.log(b)
//         b.addEventListener("click", function() {
//             removeClassToSchedule(b.data-courseCode)
//         });
//     }
// }





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
