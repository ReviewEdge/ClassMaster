import {addClassToSchedule, removeClassFromSchedule} from "./schedule.js"

function clickMoreInfo(val) {
    var popup = document.getElementById("classInfoText" + val);
    popup.classList.toggle("show");
}



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

    createClassObjectFromJSON(classInfoJSON, inSchedule) {
        // console.log(classInfoJSON)
        const p = document.createElement("p");
        p.className = 'classText';
        p.id ='classText' + this.nextUniqueID;
        const courseCodeDiv = classInfoJSON.code.split(" ");
        if(!(inSchedule)){
            p.innerText =  classInfoJSON.courseCodeWithoutTerm + ", " + classInfoJSON.term.name + ": " + classInfoJSON.title + ", Professor: " + classInfoJSON.professor;
        }
        else {
            p.innerText =  classInfoJSON.courseCodeWithoutTerm + ": " + classInfoJSON.title + ", Professor: " + classInfoJSON.professor;
        }

        if(classInfoJSON.hasOwnProperty("timeSlots")){
            p.innerText += ", " + turnTimeslotIntoString(classInfoJSON.timeSlots)
        }
        else {
            p.innerText += ", Time unlisted"
        }

        const detailsButton = document.createElement("span");
        // detailsButton.className = 'classText';
        detailsButton.id ='detailsButton' + this.nextUniqueID;
        detailsButton.innerHTML = "<br><b>Click for Details</b> ";
        // detailsButton.innerText = "(Click for Details) ";
        const intVal = this.nextUniqueID
        detailsButton.addEventListener("click", function() {
            var popup = document.getElementById('classInfoText' + intVal);
            popup.classList.toggle("show");
        });
        p.append(detailsButton)

        if(!(inSchedule)){
            const addButton = document.createElement("span");
            addButton.className = 'btn btn-success btn-sm addClassButton';
            addButton.id ='addClassButton' + this.nextUniqueID;
            addButton.innerText = "+";
            addButton.addEventListener("click", function() {
                addClassToSchedule(classInfoJSON.code)
            });
            p.append(addButton)
        }
        else {
            const removeButton = document.createElement("span");
            removeButton.className = 'btn btn-danger btn-sm removeClassButton';
            removeButton.id ='removeClassButton' + this.nextUniqueID;
            removeButton.innerText = "-";
            removeButton.addEventListener("click", function() {
                removeClassFromSchedule(classInfoJSON.code)
            });
            p.append(removeButton)
        }

        const pop = document.createElement("p");
        pop.className = 'classInfoText';
        pop.id ='classInfoText' + this.nextUniqueID;
        pop.innerText = "Credits: " + classInfoJSON.credits + ", Description/Prereqs: "  + classInfoJSON.description;
        p.append(pop);
        
        this.nextUniqueID++;
        return p;
    }
}

function turnTimeslotIntoString(timeslot){
    // console.log(timeslot)
    var str = ""
    for(const t of timeslot){
        const startSplit = t.start.split(":")
        const endSplit = t.end.split(":")
        str += t.day + " "
        let startHour = startSplit[0];
        let endHour = endSplit[0]
        let meridian1 = " AM"
        let meridian2 = " AM"

    
        if (startHour > 12) {
            startHour = startHour-12;
            meridian1 = " PM";
        }

        if (endHour > 12) {
            endHour = endHour-12;
            meridian2 = " PM";
        }
        str += startHour + ":" + startSplit[1] + " " + meridian1 + " - " + endHour + ":" + endSplit[1] + " " + meridian2 +", "
    }
    return str
}

export const coFactory = new classObjectFactory();