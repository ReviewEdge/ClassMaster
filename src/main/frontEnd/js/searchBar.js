window.addEventListener("DOMContentLoaded", function() {
    const searchButton = document.getElementById("search-submit-button");

    searchButton.addEventListener("click", function() {
        const searchBar = document.getElementById("class-search-bar");
        const searchQuery = searchBar.value;
        const container = document.getElementById("search-results");

        console.log(container);


        const getSearchURL = `http://localhost:8080/search?query=` + searchQuery;
        fetch(getSearchURL)
            .then(data => {
                data.json().then((data) => {
                    console.log(data);


                    for (const c of data) {

                        console.log(c);

                        const p = document.createElement("p");
                        p.innerText = c;
                        container.append(p);
                    }

                });
            });
    });
});