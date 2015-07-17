/**
 * Created by Admin on 2014/10/14.
 */
var bcrypt = require('bcrypt-nodejs');
var request = require('request');
var rest_api = require('../../config/rest_api'),
    _ = require('underscore');

function performanceMonitor() {

}
    performanceMonitor.startPerformanceMonitor = function(callback){
        request.post(rest_api.performance_monitor+'/setup',function(err, res,body){
        console.log("start Performance Monitor request sent to REST");
            console.log(res.body);
            if(res.body){
                callback(res.body);
                return;
            }
        });
        return;
    };

    performanceMonitor.stopPerformanceMonitor = function(){
        request.post(rest_api.performance_monitor+'/teardown',function(err,res,body){
        console.log("stop Performance Monitor request sent to REST");
            return;
        });
        return;
    };

    performanceMonitor.viewPerformanceMonitor = function(callback){
        request(rest_api.performance_monitor,  {json:true}, function(err,res,body){
            console.log("view Performance Monitor request sent to REST");
            callback(null,body);
            return;
        });
        return;
    };


module.exports = performanceMonitor;
