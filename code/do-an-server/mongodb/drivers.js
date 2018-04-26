var init = require("./init")

nameCollection = "drivers"
nameDatabase = "driper"
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
            db.db(nameDatabase).collection(nameCollection).find({phoneNumber: phoneNumber}).toArray(function (err, docs) {
                callback(docs[0])
            })
        })
    },
    findAll: function ( callback) {
        init(function (db) {
            db.db(nameDatabase).collection(nameCollection).find().toArray(function (err, docs) {
                callback(docs)
            })
        })
    },
    delete: function (json) {

    }
}