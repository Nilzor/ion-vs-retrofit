var http = require('http');

// Configure our HTTP server to respond with Hello World to all requests.
var server = http.createServer(function (request, response) {
    if (request.url.startsWith("/jsonSmall")) {
        var id =  request.url.substr(request.url.lastIndexOf("/") + 1);
        console.log("Got connection to " + request.url);
        response.writeHead(200, {"Content-Type": "application/json"});
        response.end(JSON.stringify({
            world: "Hello",
            id: id
        }));
    }
    else {
        response.writeHead(404, {"Content-Type": "text/plain"});
        response.end("Not found\n");
    }
});

// Listen on port 8000, IP defaults to 127.0.0.1
server.listen(8000, () => {
    console.log("Server listening on: http://localhost:8000");
});