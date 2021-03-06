var http = require('http');
var fs = require('fs');
var ctr = 1;

// Configure our HTTP server to respond with Hello World to all requests.
var server = http.createServer(function (request, response) {
    console.log("Got req " + ctr++);
    if (request.url.startsWith("/jsonSmall")) {
        var smallObject = getSmallObject(request);
        writeJson(smallObject, response);
    }
    else if (request.url.startsWith("/jsonLarge")) {
        writeFile(response);
    }
    else {
        response.writeHead(404, {"Content-Type": "text/plain"});
        response.end("Not found\n");
    }
});

function writeFile(response ) {
    fs.readFile('../data/giaever-og-joffen.json', function read(err, data) {
        if (err) {
            throw err;
        }
        else {
            response.writeHead(200, {});
            response.end(data);
        }
    });
}

// Listen on port 8000, IP defaults to 127.0.0.1
server.listen(8000, () => {
    console.log("Server listening on: http://localhost:8000");
});


function getSmallObject(request) {
    var id =  request.url.substr(request.url.lastIndexOf("/") + 1);
    return {
        hello: id
    };
}

function getLargeObject(request) {
    return {
        Hi: "Barbie",
        Hi: "Ken!",
        Do: "you wanna go for a ride?",
        Sure: "Ken!",
        Jump: "in...",
        Im: " a Barbie girl, in the Barbie world",
        Life: "in plastic, it's fantastic!",
        You: "can brush my hair, undress me everywhere",
        Imagination: ", life is your creation",
        Come: "on Barbie, let's go party!"
    }
}

function writeJson(obj, response) {
    response.writeHead(200, {"Content-Type": "application/json"});
    response.end(JSON.stringify(obj));
}
