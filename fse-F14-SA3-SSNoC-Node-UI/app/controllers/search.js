/**
 * Created by Admin on 2014/10/14.
 */

var searchRest = require('../models/searchRest');

module.exports = function(_, io, passport,participants) {

    return{

        getSearchPage : function(req, res) {

            var role;
            for(var i = 0; i < participants.all.length; i++) {
//                console.log("gooood " + participants.all[i].userName);
                if(participants.all[i].userName==req.session.passport.user.user_name){
                    role = participants.all[i].privilegeLevel;
                }
            }
            if (role == "Coordinator"){
                res.render('SearchCoordinator',{title:"Hello "+req.session.passport.user.user_name+" !!"});
            }
            else if (role == "Monitor"){
                res.render('SearchMonitor',{title:"Hello "+req.session.passport.user.user_name+" !!"});
            }
            else if (role == "Administrator"){
                res.render('SearchAdministrator',{title:"Hello "+req.session.passport.user.user_name+" !!"});
            }
            else{
                res.render('Search',{title:"Hello "+req.session.passport.user.user_name+" !!"});
            }

            //res.render('Search', {title: "Hello " +req.session.passport.user.user_name+" !!"} );

        },

        sendSearchQuery : function(req,res){
            var content = req.param('content');
            var type = req.param('type');
            searchRest.sendQuery(content,type,function (err,result) {
                console.log(result);
                res.json(200, {queryResult:result, queryType:type});
            });
        }/*,

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
        }*/

    };
};