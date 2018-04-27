var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var router = require("./router/init")
var realtime = require("./realtime/init")

server.listen(process.env.PORT || 81);

app.get('/', function (req, res) {
    res.send("xin chào!!!")
});
io.on('connection', function (socket) {
    realtime(socket);
    console.log('connection');
    socket.on('disconnect', function () {
        console.log('disconnect');
        socket.emit('Driver_Update_Star', {phoneNumber: '689898'});
    });
});
// var drivers = require("./mongodb/drivers")
// drivers.findOne('12345', function (jsonDriver) {
//     console.log('jsonDriver');
//     console.log(jsonDriver);
//     // socket.emit('checkDriverIsExists_Res', jsonDriver);
// })
// drivers.findAll( function (jsonDriver) {
//     console.log('jsonDriver');
//     console.log(jsonDriver);
//     // socket.emit('checkDriverIsExists_Res', jsonDriver);
// })
// customers.insertAndUpdate({abc: 'abc'})