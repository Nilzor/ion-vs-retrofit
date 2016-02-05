var http = require('http');

var doneCount = 0;
var maxCount = 40;
console.log("Doing " + maxCount + " requests...");

var doRequest = (path) => {
    var useProxy = true;
    var proxyHost = 'localhost';
    var proxyPort = 8899;

    var targetHost = 'localhost';
    var targetPort = 8000;
    var targetPath = path;

    var options = {};
    if (useProxy) {
        options.host = proxyHost;
        options.port = proxyPort;
        options.path = "http://" + targetHost + ":" + targetPort + path;
        options.headers = {
            Host: targetHost
        }
    }
    else {
        options.host = targetHost;
        options.port = targetPort;
        options.path = targetPath;
    }


    var req = http.request(options, function(res) {
        var data = '';
        res.on('data', (chunk) => {
            data += chunk;
            //console.log("Got: " + chunk);
        });
        res.on('end', () => {
            doneCount++;
        });
    });

    req.on('error', (e) => {
        console.log(`problem with request: ${e.message}`);
    });

    req.end();
};

for (var i = 0; i < maxCount; i++) {
    doRequest('/jsonLarge/' + i);
}

console.log("Done with " + maxCount + " requests");
