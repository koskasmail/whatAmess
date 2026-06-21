function callService(endpoint) {
    fetch(endpoint)
        .then(r => r.json())
        .then(data => {
            document.getElementById("output").innerHTML =
                "<pre>" + JSON.stringify(data, null, 2) + "</pre>";
        })
        .catch(err => {
            document.getElementById("output").innerHTML = "Error: " + err;
        });
}
