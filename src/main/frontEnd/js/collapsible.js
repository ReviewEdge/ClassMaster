window.addEventListener("DOMContentLoaded", function() {
    const coll = document.getElementsByClassName("collapsible-btn");
    const actColl = document.getElementsByClassName("collapsible");
    const schedsList = document.getElementById("sc-list-id");

    coll[0].addEventListener("click", function() {
        actColl[0].classList.toggle("active");
        schedsList.classList.toggle("schedules-is-active");
    });
});
