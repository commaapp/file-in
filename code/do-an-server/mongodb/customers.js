var init = require("./init")
module.exports = {
    insert: function (json) {
        init(function (db) {
            db.db("driper").collection("customers").insertOne(json, function (err, res) {
                if (err) throw err;
                console.log("1 document inserted");
                db.close();
            });
        })


    },
    insertAndUpdate: function (query, newValue, callback) {
        init(function (db) {
            db.db("driper").collection("customers").update(query, newValue, {upsert: true}, callback)
        })
    },
    findIsExists: function (phoneNumber, callback) {
        init(function (db) {
            db.db("driper").collection("customers").find({sdt: phoneNumber}).toArray(function (err, docs) {
                callback(docs[0])
            })
        })
    },
    findOne: function (phoneNumber, callback) {
        init(function (db) {
            db.db("driper").collection("customers").find({sdt: phoneNumber}).toArray(function (err, docs) {
                callback(docs[0])
            })
        })
    },
    updateNameCustom: function (query, newValue, callback) {
        init(function (db) {
            db.db("driper").collection("customers").updateOne(query, newValue, {upsert: true}, callback)
        })
    },
    delete: function (json) {

    }
}