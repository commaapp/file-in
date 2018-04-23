var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var router = require("./router/init")
var realtime = require("./realtime/init")
var customers = require("./mongodb/customers")

server.listen(process.env.PORT || 80);

app.get('/', function (req, res) {
    res.sendfile(__dirname + '/index.html');
});


io.on('connection', function (socket) {

    realtime(socket);
    console.log('connection');
    socket.on('disconnect', function () {
        console.log('disconnect');
    });

});


// customers.insertAndUpdate({abc: 'abc'})