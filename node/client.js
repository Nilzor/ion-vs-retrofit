var http = require('http');

var doneCount = 0;
var maxCount = 40;
console.log("Doing " + maxCount + " requests...");

var doRequest = (path, reqId) => {
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
            var time = (new Date).getTime() - millisStart;
            console.log("E:%d:%d:Total: ", reqId, time, doneCount);
            if (doneCount == maxCount) {
                var time = (new Date).getTime() - millisStart;
                console.log("Done with all " + doneCount + " in " + time + "ms");
            }
        });
    });

    req.on('error', (e) => {
        console.log(`problem with request: ${e.message}`);
    });

    req.end();
    var time = (new Date).getTime() - millisStart;
    console.log("S:%d:%d", reqId, time);
};

var millisStart = (new Date).getTime();
for (var i = 0; i < maxCount; i++) {
    doRequest('/jsonSmall/' + i, i);
}

console.log("Done with init");
