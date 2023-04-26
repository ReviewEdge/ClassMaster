window.addEventListener("DOMContentLoaded", function() {
    const searchButton = document.getElementById("search-submit-button");

    searchButton.addEventListener("click", function() {
        const searchBar = document.getElementById("class-search-bar");
        const searchQuery = searchBar.value;
        const container = document.getElementById("search-results");

        const getSearchURL = `http://localhost:8080/search?query=` + searchQuery;
        fetch(getSearchURL)
            .then(data => {
                data.json().then((data) => {
                    if (data.length === 0) {
                        container.innerHTML = "<p>No search results</p>";
                    } else {
                        container.innerHTML = "";
                        for (const c of data) {
                            const p = document.createElement("p");
                            p.innerText = c;
                            container.append(p);
                        }
                    }

                });
            });

    });
});