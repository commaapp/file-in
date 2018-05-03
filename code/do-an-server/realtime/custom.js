var customers = require("../mongodb/customers")
var drivers = require("../mongodb/drivers")
module.exports = function (socket) {

    //trước khi custemer đăng nhập gồm sdt + tên  thì gửi sdt lên hệ thông để xác nhận số điện thoại
    socket.on('Vefify_Phonenumber', function (data) {
        setTimeout(function () {
            // tạo mã xác nhận và gửi lại usẻ để xác nhận lại điện thoại
            verifyCode = Math.floor(1000 + Math.random() * 9000);
            socket.emit('Vefify_Code_Res', verifyCode);
        }, 3000);
    })

    // đnăg nhập gồm tên + sdt
    socket.on('Custemer_Login', function (data) {
        data = JSON.parse(data)
        customers.insertAndUpdate({sdt: data.sdt}, data, function (err, count, res) {
            if (err) throw err;
        });
        socket.emit('Custemer_Login_Res', "");
    })

    // lưu dữ liệu ng dùng trong máy và khi vào lại app tự động đnăg nhập
    // check xem acc luu trong máy dã có trến server chưa
    socket.on('Custemer_Check_Login', function (data) {
        customers.findIsExists(data, function (acc) {
            socket.emit('Custemer_Check_Login_Res', acc);
        })
    })
    socket.on('Custemer_View_Profile', function (data) {
        customers.findIsExists(data, function (acc) {
            socket.emit('Custemer_View_Profile_Res', acc);
        })
    })
    socket.on('Custemer_Update_Profile', function (data) {
        data = JSON.parse(data);
        customers.updateNameCustom({sdt: data.sdt}, {$set: {name: data.name}}, function () {
            socket.emit('Custemer_Update_Profile_Res', "");
        })
    })
    socket.on('updateStateOnlineProfile', function (data) {
        data = JSON.parse(data);
        console.log('----------------------------- update State Online Profile')
        console.log(data)
        customers.updateStateOnlineProfile({sdt: data.sdt}, {$set: {isOnline: data.isOnline}}, function () {
        })
    })
    socket.on('updateCustomLocation', function (data) {
        data = JSON.parse(data)
        customers.updateLocation(data, function () {

        })
    })
    socket.on('findDriperInLocation', function (data) {
        data = JSON.parse(data)
        drivers.findReadyInLocation({isReady: true}, function (array) {
            socket.emit('findDriperInLocation_res', array);
        })
    })
}