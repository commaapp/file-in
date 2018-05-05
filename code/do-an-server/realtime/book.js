var books = require("../mongodb/books")
var customers = require("../mongodb/customers")
var drivers = require("../mongodb/drivers")


var rad = function (x) {
    return x * Math.PI / 180;
};
a = {
    lat: 21.0511051,
    lng: 105.7404893,
}
b = {
    lat: 21.057926700000003,
    lng: 105.7307161,
}
var getDistance = function (p1, p2) {
    var R = 6378137; // Earth’s mean radius in meter
    var dLat = rad(p2.lat - p1.lat);
    var dLong = rad(p2.lng - p1.lng);
    var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
        Math.cos(rad(p1.lat)) * Math.cos(rad(p2.lat)) *
        Math.sin(dLong / 2) * Math.sin(dLong / 2);
    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    var d = R * c / 1000;
    return d; // returns the distance in meter
};


function findNewDriver(data, truDriver, socket, io) {
    // console.log(io)
    var booklatlng = {
        lat: data.latFrom,
        lng: data.lngFrom
    }
    console.log('truDriver ' + truDriver)
    // tìm tài xế sẵn snagf và gần nhất theo vị trí
    drivers.findReadyInLocation({isReady: true, phoneNumber: {$ne: truDriver}}, function (array) {
        console.log(array.length)
        if (array != null) {
            for (i in array) {
                array[i].distance = getDistance(
                    {
                        lat: array[i].lat,
                        lng: array[i].lng,
                    }, booklatlng)
                console.log(array[i].distance)
            }
            var minDriver = array[0]

            for (i in array) {
                if (minDriver.distance > array[i].distance)
                    minDriver = array[i]
            }


            try {
                if (array[i].distance > 2) {
                    console.log('tài xế xa hơn 2km không tìm thấy tài xế nào')
                    socket.emit('bookFindDriver_res', "null");
                }
                else {
                    // socket.emit('bookFindDriver_res', minDriver);
                    console.log('nhỏ nhất là ' + minDriver.name)
                    console.log('nhỏ nhất là ' + minDriver.distance)
                    data.phoneDriver = minDriver.phoneNumber

                    //thêm đơn hàng đang chờ
                    console.log('insertAndUpdate data ' + JSON.stringify(data))
                    books.insertAndUpdate({phoneCutomer: data.phoneCutomer}, data, function (err, count, res) {
                        if (err) throw err;
//cập nhật lại trạng thái cho tài xế
//                         drivers.updateState({phoneNumber: minDriver.phoneNumber, isReady: false}, function () {
                        //Thông báo có đơn hàng mới
                        io.sockets.emit("notify", "Có cuốc xe mới");
                        io.sockets.emit("NEW_BOOK", data);
                        //     }
                        // )

                    })

                }
            } catch (e) {
                console.log('không tìm thấy tài xế nào')
                socket.emit('bookFindDriver_res', "null");
            }

        } else {
            socket.emit('bookFindDriver_res', "null");
            // không tìm thấy tài xế nào
            console.log('không tìm thấy tài xế nào')
        }


    })

}

module.exports = function (socket, io) {

    socket.on('bookFindDriver', function (data) {
        data = JSON.parse(data);
        console.log("findNewDriver")
        findNewDriver(data, "", socket, io);
    })
    socket.on('tuChoiCuoc', function (truDriver) {
        console.log("yêu cầu hủy cuốc từ " + truDriver)
        // cập nhật lại trạng thái cho tài xế
        //tìm cuốc xe vừa tài xế đã hủy
        books.findOne({phoneDriver: truDriver}, function (book) {
            findNewDriver(book, truDriver, socket, io);
        })
    })
// gửi thông tin cuốc xe cho cả 2 ng
    socket.on('chapNhanCuoc', function (truDriver) {
        books.findOne({phoneDriver: truDriver}, function (book) {
            io.sockets.emit("notify", "Cuốc xe mới được chấp nhận");
            drivers.findOne(book.phoneDriver, function (driver) {
                book.driver = driver
                book.driver.imCMT1 = ""
                book.driver.imCMT2 = ""
                book.driver.imBLX1 = ""
                book.driver.imBLX2 = ""
                book.driver.imGTX1 = ""
                book.driver.imCMT1 = ""
                book.driver.imGTX2 = ""
                book.driver.imBaoHiem1 = ""
                book.driver.imBaoHiem2 = ""
                io.sockets.emit("NEW_OK_BOOK_CUSTOMER", book);
                customers.findOne(book.phoneCutomer, function (customer) {
                    book.customer = customer
                    io.sockets.emit("NEW_OK_BOOK_DRIVER", book);
                })
            })


        })
    })
}