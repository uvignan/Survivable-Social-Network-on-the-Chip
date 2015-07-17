/**
 * Created by Admin on 2014/10/14.
 */
var bcrypt = require('bcrypt-nodejs');
var request = require('request');
var rest_api = require('../../config/rest_api'),
    _ = require('underscore');

function MemoryMonitor() {

}
    MemoryMonitor.startMemoryMonitor = function(){
        request.post(rest_api.memory_monitor+'/start',function(err, res, body){
        console.log("start Memory Monitor request sent to REST");
            return;
        });
        return;
    };

    MemoryMonitor.stopMemoryMonitor = function(){
        request.post(rest_api.memory_monitor+'/stop',function(err,res,body){
        console.log("stop Memory Monitor request sent to REST");
            return;
        });
        return;
    };
    MemoryMonitor.deleteMemoryHistory = function(){
        request.del(rest_api.memory_monitor,function(err,res,body){
            console.log("delete Memory History request sent to REST");
            return;
        });
        return;
    };

    MemoryMonitor.getMemoryStats = function(callback) {
    request(rest_api.memory_monitor, {json:true}, function(err, res, body) {
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

module.exports = MemoryMonitor;
