window.onload = function(){
    const coll = document.getElementsByClassName("collapsible-btn");
    const actColl = document.getElementsByClassName("collapsible");

    coll[0].addEventListener("click", function() {
        actColl[0].classList.toggle("active");
    });
};