
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

    createClassObject(classInfo) {
        console.log(classInfo)
        const p = document.createElement("p");
        p.className = 'classText';
        p.id ='classText' + this.nextUniqueID;
    //    p.innerText = ClassInfo + "     (•••)";
        p.innerText = classInfo + "     (Click for Details)";
        p.setAttribute('onclick', 'clickMoreInfo(' + this.nextUniqueID + ')');

        const pop = document.createElement("p");
        pop.className = 'classInfoText';
        pop.id ='classInfoText' + this.nextUniqueID;
        pop.innerText = classInfo;
        p.append(pop);
        this.nextUniqueID++;
        return p;
    }
}

coFactory = new classObjectFactory();