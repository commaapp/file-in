custom_login = require("./custom")
driver_login = require("./driver")
book = require("./book")
module.exports = function (socket, io) {
    socket.emit('CLIENT_CUSTOMER_CONNECT_SUCCES', {hello: 'CLIENT_CUSTOMER_CONNECT_SUCCES'});

    custom_login(socket)
    driver_login(socket)
    book(socket, io)

}