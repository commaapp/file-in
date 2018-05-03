var drivers = require("../mongodb/drivers")
var customers = require("../mongodb/customers")
module.exports = function (socket) {
    socket.on('Driver_Regitor', function (data) {
        data = JSON.parse(data);
        drivers.insertAndUpdate({phoneNumber: data.phoneNumber}, data, function () {
            socket.emit('Driver_Regitor_Res', "Đăng ký thành công. Chúc mừng bạn đã trở thành đối tác của Driper!!!");
        })
    })
    socket.on('Driper_Vefify_Phonenumber', function (data) {
        setTimeout(function () {
            verifyCode = Math.floor(1000 + Math.random() * 9000);
            socket.emit('Driper_Vefify_Code_Res', verifyCode);
        }, 3000);
    })
    socket.on('checkDriverIsExists', function (phoneNumber) {
        drivers.findOne(phoneNumber, function (jsonDriver) {
            socket.emit('checkDriverIsExists_Res', jsonDriver);
        })
    })
    socket.on('getDriverProfile', function (phoneNumber) {
        drivers.findOne(phoneNumber, function (jsonDriver) {
            socket.emit('getDriverProfile_Res', jsonDriver);
        })
    })
    socket.on('Driver_Update_Location', function (data) {
        data = JSON.parse(data)
        drivers.updateLocation(data, function () {

        })
    })
    socket.on('Driver_Update_State', function (data) {
        data = JSON.parse(data)
        drivers.updateState(data, function () {

        })
    })
    socket.on('Driver_Update_Star', function (data) {
        data = JSON.parse(data)
        drivers.updateStar(data, function () {

        })
    })
    socket.on('getCustomerOnline', function (data) {
        customers.getCutemerOnline(function (array) {
            socket.emit('getCustomerOnline_Res', array);
            console.log(array)
        })
    })


}