custom_login = require("./custom")
driver_login = require("./driver")
module.exports = function (socket) {
    socket.emit('CLIENT_CUSTOMER_CONNECT_SUCCES', {hello: 'CLIENT_CUSTOMER_CONNECT_SUCCES'});

    custom_login(socket)
    driver_login(socket)

}