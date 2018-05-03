var init = require("./init")

var nameDatabase = "driper"
var nameCollection = "books"
module.exports = {
    //thêm một đơn hàng mới
    insertAndUpdate: function (query, newValue, callback) {
        // console.log('query- newValue')
        // console.log(query)
        // console.log(newValue)
        init(function (db) {
            db.db(nameDatabase).collection(nameCollection).update(query, newValue, {upsert: true}, callback)
        })
    },
    findOne: function (query, callback) {
        init(function (db) {
            db.db(nameDatabase).collection(nameCollection).findOne(query, function (err, result) {
                if (err) throw err;
                console.log(result)
                callback(result)
                return
            });
        })
    },
}