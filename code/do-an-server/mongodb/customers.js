var init = require("./init")
var nameDatabase = "driper"
var nameCollection = "customers"
module.exports = {
    insert: function (json) {
        init(function (db) {
            db.db(nameDatabase).collection(nameCollection).insertOne(json, function (err, res) {
                if (err) throw err;
                db.close();
            });
        })
    },
    insertAndUpdate: function (query, newValue, callback) {
        init(function (db) {
            db.db(nameDatabase).collection(nameCollection).update(query, newValue, {upsert: true}, callback)
        })
    },
    findIsExists: function (phoneNumber, callback) {
        init(function (db) {

            db.db(nameDatabase).collection(nameCollection).findOne({sdt: phoneNumber}, function (err, result) {
                if (err) throw err;
                console.log(result)
                callback(result)
            });

            // db.db(nameDatabase).collection(nameCollection).find({sdt: phoneNumber}).toArray(function (err, docs) {
            //     callback(docs[0])
            // })
        })
    },
    findOne: function (phoneNumber, callback) {
        init(function (db) {
            // db.db(nameDatabase).collection(nameCollection).find({sdt: phoneNumber}).toArray(function (err, docs) {
            //     callback(docs[0])
            // })
            db.db(nameDatabase).collection(nameCollection).findOne({sdt: phoneNumber}, function (err, result) {
                if (err) throw err;
                console.log(result)
                callback(result)
            });

        })
    },
    findAll: function (query, callback) {
        init(function (db) {
            db.db(nameDatabase).collection(nameCollection).find(query).toArray(function (err, docs) {
                callback(docs)
            })
        })
    },
    updateNameCustom: function (query, newValue, callback) {
        init(function (db) {
            db.db(nameDatabase).collection(nameCollection).updateOne(query, newValue, {upsert: true}, callback)
        })
    },
    updateStateOnlineProfile: function (query, newValue, callback) {
        init(function (db) {

            db.db(nameDatabase).collection(nameCollection).updateOne(query, newValue, {upsert: true}, callback)
        })
    },
    updateLocation: function (data, callback) {
        init(function (db) {
            db.db(nameDatabase).collection(nameCollection).update({sdt: data.sdt}, {
                $set: {
                    'lat': data.lat,
                    'lng': data.lng
                }
            }, {upsert: true}, callback)

        })
    },
    getCutemerOnline: function (callback) {
        init(function (db) {
            db.db(nameDatabase).collection(nameCollection).find({isOnline: true}).toArray(function (err, docs) {
                callback(docs)
            })
        })
    },
    delete: function (json) {

    }
}