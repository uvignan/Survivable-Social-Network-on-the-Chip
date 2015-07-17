/**
 * Created by Admin on 2014/10/14.
 */


var SSNanalysis = require('../models/SSNRest')

module.exports = function(_, io,participants, passport) {

    return{

        getSSNanalysisPage : function(req, res) {
            for(var i = 0; i < participants.all.length; i++) {
                console.log("gooood   " + participants.all[i].userName);
                var role;
                if(participants.all[i].userName==req.session.passport.user.user_name){
                    role = participants.all[i].privilegeLevel;
                }
            }
            if (role == "Administrator") {
                res.render('SSNanalysisAdmin', {title: "Hello " + " !!"});
            }
            else{
                res.render('SSNanalysis', {title: "Hello " + " !!"});
            }
        },

        setStartSSNanalysis : function(req,res){
            console.info('inside setStartSSNanalysis.js');
            if (req.param('startTime') == "infinite"){
                var startTime = "1900-01-01 00:00"; //cite parameter here-----
            }
            else{
                var startTime = req.param('startTime');
            }

            var endTime;

            var datenow = new Date();
            if (req.param('endTime') == "infinite"){
                endTime = datenow.getFullYear() + "-" + (datenow.getMonth()+1) + "-" + datenow.getDate() + " " +datenow.getHours() + ":" +datenow.getMinutes();
            }
            else{
                endTime = req.param('endTime');
            }
            console.info(endTime);


            console.info('date assignment finished');
            SSNanalysis.startSSNanalysis(startTime,endTime,function(err,results){
                if(err){
                    console.info('errrrrrrrrrrrrrrrr');
                }
                if(results){

                    console.info('Can get results');
                    res.json(200,results);
                }
            });


        }


    };
};