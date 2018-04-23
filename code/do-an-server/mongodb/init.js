module.exports = function (callback) {
    var MongoClient = require('mongodb').MongoClient;
    var url = "mongodb://driper:driper@ds157057.mlab.com:57057/driper";

    MongoClient.connect(url, function (err, db) {
        if (err) throw err;
        var dbo = db.db("driper");
        dbo.createCollection("customers", function (err, res) {
            if (err) throw err;
            console.log("Collection created!");
            db.close();
        });
        callback(db)
    });
}