var init = require("./init")
var nameCollection = "drivers"
var nameDatabase = "driper"
module.exports = {

    insert: function (json) {
        init(function (db) {
            db.db(nameDatabase).collection(nameCollection).insertOne(json, function (err, res) {
                if (err) throw err;
                console.log("1 document inserted");
                db.close();
            });
        })
    },
    insertAndUpdate: function (query, newValue, callback) {
        init(function (db) {
            db.db(nameDatabase).collection(nameCollection).update(query, newValue, {upsert: true}, callback)
        })
    },

    findOne: function (phoneNumber, callback) {
        init(function (db) {
            console.log('findOne')
            console.log('phoneNumber ' + phoneNumber)
            console.log('nameCollection ' + nameCollection)

            db.db(nameDatabase).collection(nameCollection).findOne({phoneNumber: phoneNumber}, function (err, result) {
                if (err) throw err;
                // console.log(result)
                callback(result)
            });
            // db.db(nameDatabase).collection(nameCollection).find({phoneNumber: phoneNumber}).toArray(function (err, docs) {
            //     callback(docs[0])
            // })
        })
    },
    findAll: function (callback) {
        init(function (db) {
            db.db(nameDatabase).collection(nameCollection).find().toArray(function (err, docs) {
                callback(docs)
            })
        })
    },
    delete: function (json) {

    },
    updateLocation: function (data, callback) {
        init(function (db) {
            // console.log(data.phoneNumber);
            db.db(nameDatabase).collection(nameCollection).update({phoneNumber: data.phoneNumber}, {
                $set: {
                    'lat': data.lat,
                    'lng': data.lng,
                    'degree': data.degree,
                }
            }, {upsert: true}, callback)

        })
    },
    updateState: function (data, callback) {
        console.log('updateState')
        init(function (db) {
            // console.log(data.phoneNumber);
            db.db(nameDatabase).collection(nameCollection).update({phoneNumber: data.phoneNumber}, {
                $set: {
                    'isReady': data.isReady,
                }
            }, {upsert: true}, callback)

        })
    },
    updateStar: function (data, callback) {
        init(function (db) {
            // console.log(data.phoneNumber);
            db.db(nameDatabase).collection(nameCollection).update({phoneNumber: data.phoneNumber}, {
                $set: {
                    'ReviewNumber': 10,
                    'StarNumber': 11
                }
            }, {upsert: true}, callback)

        })
    }, findReadyInLocation: function (query, callback) {
        init(function (db) {
            db.db(nameDatabase).collection(nameCollection).find(query).toArray(function (err, docs) {
                callback(docs)
            })
        })
    },


}