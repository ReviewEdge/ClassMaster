
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
            addButton.setAttribute('onclick', 'addClassToSchedule("' + classInfoSplit[0] +'")');
            p.append(addButton)
        }
        else {
            const removeButton = document.createElement("span");
            removeButton.className = 'removeClassButton';
            removeButton.id ='removeClassButton' + this.nextUniqueID;
            removeButton.innerText = "(Click to remove Class)";
            removeButton.setAttribute('onclick', 'removeClassFromSchedule("' + classInfoSplit[0] +'")');
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

coFactory = new classObjectFactory();