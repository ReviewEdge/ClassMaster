import {coFactory} from "./courseInfo.js"
let classes = null;//store a list of classes from last time filter got ran

window.addEventListener("DOMContentLoaded", function() {
    // attach an event listener to the search bar for dynamic updates
    updateFilter();
    const searchBar = document.getElementById("class-search-bar");
    searchBar.addEventListener("input", onType);
    searchBar.addEventListener("focus", onFocusSearch);
    const addTimeBtn = this.document.getElementById("add-time");
    addTimeBtn.addEventListener("click", addTime);
    const clearTimeBtn = this.document.getElementById("clear-time");
    clearTimeBtn.addEventListener("click", clearTime);
    // document.getElementById("filters-button").addEventListener("click", updateFilter);
});

function addTime(){
    const new_timeslot = document.getElementById("timeslot-hidden").cloneNode(true);
    new_timeslot.removeAttribute("hidden");
    new_timeslot.removeAttribute("id");
    new_timeslot.childNodes.item(1).addEventListener("click", function(e){
        e.target.parentElement.parentElement.removeChild(e.target.parentElement);
    })
    document.getElementById("timeslots").appendChild(new_timeslot);
}

function clearTime(){
    const parent = document.getElementById("timeslots");
    while (parent.firstChild) {
        parent.removeChild(parent.firstChild);
    }
}

function onFocusSearch(){
    //close and submit the filter if it hasn't been submitted already
    if(document.getElementById("filterCollapse").classList.contains("show")){
        classes = null;
        document.getElementById("filters-button").click();
    }
    updateFilter();
}

function onType(){
    //filter the currently stored classes however they are stored
    if(classes === null){
        return;
    }
    const container = document.getElementById("search-results");
    const search = document.getElementById("class-search-bar").value.toLowerCase().trim();
    container.innerHTML = "";
    // console.log(classes)

    for (const c of classes) {
        // console.log(classObjectToString(c))
        if(!classObjectToString(c).toLowerCase().includes(search)) continue;
        const p = coFactory.createClassObjectFromJSON(c, false)
        // const p = coFactory.createClassObject(c, false)
        container.append(p);
    }
    if(classes.length === 0){
        container.innerHTML = "<p>No search results</p>";
    }
}

function classObjectToString(c){
    // console.log(c)
    var str = c.code + " " + c.professor + " " + c.title + " " + c.department + " " + c.term.name + " " + c.description
    if(c.hasOwnProperty("timeSlots")){
        for(const t of c.timeSlots){
            str += termObjectToString(t)
        }
    }
    return str;
}

function termObjectToString(c){
    // console.log(c)
    var str = c.day + " " + c.start + " " + c.end
    return str;
}

async function updateFilter(){
    const prof = document.getElementById("prof-in").value;
    const code = document.getElementById("code-in").value;
    const term_elem = document.getElementById("curr-sched-term-name");
    let term = "";
    if(term_elem !== null){
        term = term_elem.innerText;
    }
    let min = document.getElementById("min-cred-in").value;
    if(min === "") min="-1";
    let max = document.getElementById("max-cred-in").value;
    if(max === "") max="-1";
    const dept = document.getElementById("dept-in").value;
    const timeslot_elems = document.getElementById("timeslots").children;
    const timeslots = new Array(timeslot_elems.length);
    //convert all timeslot elements into a string that can be decoded later
    for(var i=0; i<timeslot_elems.length; i++){
        const children = timeslot_elems.item(i).children;
        /**
         * each element has these children:
         * 1. close button
         * 2. day of week
         * 3. "from" text
         * 4. start time
         * 5. "to" text
         * 6. end time
         */
        let ret = "";

        //day, always filled so no need to check
        ret = ret + children.item(1).value + " ";

        if(children.item(3).value !== ""){//start
            ret = ret + children.item(3).value + " - ";
        }
        else continue;

        if(children.item(5).value !== ""){//end
            ret = ret + children.item(5).value;
        }
        else continue;

        timeslots[i] = ret;
    }
    const postCommentUrl = "http://localhost:8080/API/setFilter";
    const data = await fetch(postCommentUrl, {
    // fetch(postCommentUrl, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            professor : prof,
            code : code,
            minimum : min,
            maximum : max,
            department :dept,
            timeslots : timeslots,
            term : term
        })
    })
    //     .then(validateJSON)
    //     //get all classes
    //     .then(data => {
    //         classes = data;
    //     });

    const dataJSON = await data.json()

    if(dataJSON != null){
        classes = []
        for(const c of dataJSON){
            classes.push(JSON.parse(c))
        }
        // console.log(classes)
    }
}

/**
 * Validate a response to ensure the HTTP status code indcates success.
 * 
 * @param {Response} response HTTP response to be checked
 * @returns {object} object encoded by JSON in the response
 */
function validateJSON(response) {
    if (response.ok) {
        return response.json();
    } else {
        return Promise.reject(response);
    }
}




/**
 * copied from an example in web class, will be using as a reference for how to do js
 */

/*
window.addEventListener("DOMContentLoaded", function() {
    // on page load, load all current comments
    loadComments();
    // attach an event listener to the comment button to post comments
    const commentButton = document.getElementById("comment-button");
    commentButton.addEventListener("click", postComment);
});

async function loadComments() {
    const articleID = document.getElementById("articleID").value;
    const getCommentsURL = `/api/v1/comments/${articleID}/`;
    fetch(getCommentsURL)
        .then(validateJSON)
        .then(data => {
            for(const comment of data.comments){
                insertComment(comment);
            }
        });
}

function insertComment(comment) {
    const container = document.getElementById("comment-list");
    const commentHolder = document.createElement("div");
    commentHolder.classList.add("comment");
    commentHolder.innerText = comment.text;
    container.appendChild(commentHolder);
}

async function postComment() {
    const articleID = document.getElementById("articleID").value;
    const text = document.getElementById("comment-field").value;
    const postCommentUrl = `/api/v1/comments/${articleID}/`;
    fetch(postCommentUrl, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            "text":text
        })
    })
        .then(validateJSON)
        .then(insertComment);
}*/
