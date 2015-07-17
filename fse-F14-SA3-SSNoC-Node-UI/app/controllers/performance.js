/**
 * Created by Admin on 2014/10/14.
 */

var performanceMonitor = require('../models/performanceMonitorRest');

module.exports = function(_, io, participants,passport) {

    return{

        getPerformanceMeasurePage : function(req, res) {
            for(var i = 0; i < participants.all.length; i++) {
                console.log("gooood   " + participants.all[i].userName);
                var role;
                if(participants.all[i].userName==req.session.passport.user.user_name){
                    role = participants.all[i].privilegeLevel;
                }
            }
            if (role =="Administrator") {
                res.render('PerformanceMeasureAdmin', {title: "Hello " + req.session.passport.user.user_name + " !!"});
            }
            else{
                res.render('PerformanceMeasure', {title: "Hello " + req.session.passport.user.user_name + " !!"});
            }
        },

        setUpPerformanceMonitor : function(req,res){
            performanceMonitor.startPerformanceMonitor(function (body) {
                if(body){
                    res.json(body);
                }
            });
        },

        tearDownPerformanceMonitor : function(req,res){
            performanceMonitor.stopPerformanceMonitor();

            res.json(200, {});
        },

        viewPerformanceMonitor : function(req,res){
            performanceMonitor.viewPerformanceMonitor(function(err, result) {
                console.log("cool" +result);
                if (result !== null) {
                  res.json(200, {postsPerSecond:result.postsPerSecond, getPerSecond:result.getPerSecond});
                }
            });
        }

    };
};