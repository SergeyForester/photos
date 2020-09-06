function APIRequest(url, func, request = "GET", data = null) {
    let el;

    if (request === 'POST' || request === "PUT") {
        el = {'method': request, body: JSON.stringify(data)}
    } else {
        el = {'method': request};
    }

    fetch(url, el)
        .then(
            function (response) {
                if (response.status !== 200) {
                    console.log('Looks like there was a problem. Status Code: ' +
                        response.status);
                    return;
                }

                // Examine the text in the response
                response.json().then(function (data) {
                    func(data)
                });
            }
        )
        .catch(function (err) {
            console.log('Fetch Error :-S', err);
        });
}

$(".post").click(function () {
    console.log("username: " + $(this).data("name"));
    $("#modalLongTitle").text($(this).data("name"));

    console.log("Images: " + $(this).children("img"));
    console.log($(`#photos-${$(this).data("id")}`).find('img'));
    console.log($(`#photos-${$(this).data("id")}`));
    console.log(`#photos-${$(this).data("id")}`);
    console.log($(this));

    let images = $(`#photos-${$(this).data("id")}`).find('img').map(function(){
        return $(this).attr('src')
    }).get();

    for (let el of images) {
        $("#modal-body").append(`<img src="${el}" style="width: 100%;">`);
    }
    $("#modal-body").append(`<p>${$(this).data("text")}</p>`);
});

$("#modalLong").on("hidden.bs.modal", function () {
    $("#modal-body").html('');
});

function findGetParameter(parameterName) {
    let result = null,
        tmp = [];
    location.search
        .substr(1)
        .split("&")
        .forEach(function (item) {
            tmp = item.split("=");
            if (tmp[0] === parameterName) result = decodeURIComponent(tmp[1]);
        });
    return result;
}

function sendDelete(url) {
    $.ajax({
        url: url,
        method: 'delete',
    })
}