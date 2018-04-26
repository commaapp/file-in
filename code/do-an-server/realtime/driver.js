var drivers = require("../mongodb/drivers")

module.exports = function (socket) {
    socket.on('Driver_Regitor', function (data) {
        data = JSON.parse(data);
        console.log(data)
        drivers.insertAndUpdate({phoneNumber: data.phoneNumber}, data, function () {
            socket.emit('Driver_Regitor_Res', "Đăng ký thành công. Chúc mừng bạn đã trở thành đối tác của Driper!!!");
        })
    })
    socket.on('Driper_Vefify_Phonenumber', function (data) {
        console.log(data);
        setTimeout(function () {
            // tạo mã xác nhận và gửi lại usẻ để xác nhận lại điện thoại
            verifyCode = Math.floor(1000 + Math.random() * 9000);
            socket.emit('Driper_Vefify_Code_Res', verifyCode);
        }, 3000);
    })
    socket.on('checkDriverIsExists', function (phoneNumber) {
        console.log('checkDriverIsExists');
        console.log(phoneNumber);
        drivers.findOne(phoneNumber, function (jsonDriver) {
            console.log('jsonDriver');
            console.log(jsonDriver);
            socket.emit('checkDriverIsExists_Res', jsonDriver);
        })
    })
    socket.on('getDriverProfile', function (phoneNumber) {
        console.log('getDriverProfile ' + phoneNumber);
        drivers.findOne(phoneNumber, function (jsonDriver) {
            console.log('getDriverProfile ' + jsonDriver);
            socket.emit('getDriverProfile_Res', jsonDriver);
        })
    })

}