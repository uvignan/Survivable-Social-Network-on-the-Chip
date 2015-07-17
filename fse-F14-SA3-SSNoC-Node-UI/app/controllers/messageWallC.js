/**
 * Created by Vignan on 10/15/2014.
 */

var messageWallR = require('../models/MessageWallRest');

module.exports = function(_,io,participants,passport){
    return{
       getPublicWallPage : function(req,res){

           var role;
           for(var i = 0; i < participants.all.length; i++) {
//                console.log("gooood " + participants.all[i].userName);
               if(participants.all[i].userName==req.session.passport.user.user_name){
                   role = participants.all[i].privilegeLevel;
               }
           }
           if (role == "Coordinator"){
               res.render('messageWallCoordinator',{title:"Hello "+req.session.passport.user.user_name+" !!"});
           }
           else if (role == "Monitor"){
               res.render('messageWallMonitor',{title:"Hello "+req.session.passport.user.user_name+" !!"});
           }
           else if (role == "Administrator"){
               res.render('messageWallAdministrator',{title:"Hello "+req.session.passport.user.user_name+" !!"});
           }
           else{
               res.render('messageWall',{title:"Hello "+req.session.passport.user.user_name+" !!"});
           }
           // res.render('messageWall', {title: "Hello " +req.session.passport.user.user_name+" !!"} );
        },

        getPublicWallPageInfo : function(req,res){
            var role;
            messageWallR.getWallMessages(role,function(err,body){
                res.json(200, {});
            });
        },

       sendWallMessage : function(req,res){
           console.log('realtest'+req.body);
            var user_name = req.session.passport.user.user_name;
            var content = req.body.message;
            messageWallR.sendWallMessage(user_name,content,null,req.body.latitude,req.body.longitude);
                //if(body=="wall message saved"){res.render('messageWall', {message: req.flash('Message updated on Wall')} );}
                var role;
                for(var i = 0; i < participants.all.length; i++) {
//                console.log("gooood " + participants.all[i].userName);
                    if(participants.all[i].userName==req.session.passport.user.user_name){
                        role = participants.all[i].privilegeLevel;
                    }
                }
                if (role == "Coordinator"){
                    res.render('messageWallCoordinator',{title:"Hello "+req.session.passport.user.user_name+" !!"});
                }
                else if (role == "Monitor"){
                    res.render('messageWallMonitor',{title:"Hello "+req.session.passport.user.user_name+" !!"});
                }
                else if (role == "Administrator"){
                    res.render('messageWallAdministrator',{title:"Hello "+req.session.passport.user.user_name+" !!"});
                }
                else{
                    res.render('messageWall',{title:"Hello "+req.session.passport.user.user_name+" !!"});
                }

       },

        sendTestWallMessage : function (req,res){
            var user_name = "tester";
            var content = req.body.message;
//            messageWallR.sendWallMessage(user_name,content,null,function(body){
//                if(body){
//                    res.json(body);
//                }
//            });

        }
    }
};