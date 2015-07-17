/**
 * Created by Admin on 2014/10/14.
 */
var memoryMonitor = require('../models/MemoryMonitorRest');

module.exports = function(_, io,participants,passport) {

    return{

        getMemoryMeasurePage : function(req, res) {
            for(var i = 0; i < participants.all.length; i++) {
                console.log("gooood   " + participants.all[i].userName);
                var role;
                if(participants.all[i].userName==req.session.passport.user.user_name){
                    role = participants.all[i].privilegeLevel;
                }
            }
            if (role == "Administrator") {
                res.render('memoryMeasureAdmin', {title: "Hello " + req.session.passport.user.user_name + " !!"});
            }
            else{
                res.render('memoryMeasure', {title: "Hello " + req.session.passport.user.user_name + " !!"});
            }
        },

        setStartMemoryMonitor : function(req,res){
            console.info("inside setStartMemoryMonitor.js");
            memoryMonitor.startMemoryMonitor();
            res.render('memoryMeasure', {message: req.flash('Memory Measurement Started...')} );
        },

        setStopMemoryMonitor : function(req,res){
            console.info("inside setStopMemoryMonitor.js");
            memoryMonitor.stopMemoryMonitor();
            res.render('memoryMeasure', {message: req.flash('Memory Measurement Stopped...')} );
        },

        setDeleteMemoryHistory : function(req,res){
            console.info("inside setDeleteMemoryHistory.js");
            memoryMonitor.deleteMemoryHistory();
            res.render('memoryMeasure', {message: req.flash('Memory Deleted Successfully...')} );
        },

        getMeasureMemoryStats : function(req,res){
            console.info('inside getMeasureMemoryStats.js');
            memoryMonitor.getMemoryStats(function(err,results){
                if(results){
                    res.json(200,results);
                }
            });
        }


    };
};