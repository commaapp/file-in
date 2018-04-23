customlogin = require("./custom_login")
module.exports = function (socket) {
    socket.emit('CLIENT_CUSTOMER_CONNECT_SUCCES', JSON.stringify({hello: 'CLIENT_CUSTOMER_CONNECT_SUCCES'}));



    customlogin(socket)




}