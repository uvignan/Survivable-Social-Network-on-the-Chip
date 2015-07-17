/**
 * Created by Admin on 2014/10/14.
 */
var bcrypt = require('bcrypt-nodejs');
var request = require('request');
var rest_api = require('../../config/rest_api'),
    _ = require('underscore');

function SSNanalysis(startTime, endTime) {
    this.local = {
        startTime : startTime,
        endTime : endTime

    };

}
    SSNanalysis.startSSNanalysis = function(startTime, endTime, callback) {
        console.log(startTime);
        console.log(endTime);
    request(rest_api.SSN_analysis +startTime+'/'+endTime, {json:true}, function(err, res, body) {

        if (err){
            callback(err,res);
            return;
        }
        if (res.statusCode === 200) {
            callback(null, body);
            return;
        }
        if (res.statusCode !== 200) {
            callback(null, null);
            return;
        }
    });
};



module.exports = SSNanalysis;
