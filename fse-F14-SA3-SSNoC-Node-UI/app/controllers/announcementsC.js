/**
 * Created by Vignan on 10/24/2014.
 */

var annRest = require('../models/announcementsRest');

module.exports = function(_, io, participants, passport,refreshAllUsers) {
    return{
        getAnnoucementsPage : function(req,res){
            var role;
            for(var i = 0; i < participants.all.length; i++) {
//                console.log("gooood " + participants.all[i].userName);
                if(participants.all[i].userName==req.session.passport.user.user_name){
                     role = participants.all[i].privilegeLevel;
                }
            }
            if (role == "Coordinator"){
                res.render('Announcements',{title:"Hello "+req.session.passport.user.user_name+" !!"});
            }
            else if (role == "Monitor"){
                res.render('AnnouncementsMonitor',{title:"Hello "+req.session.passport.user.user_name+" !!"});
            }
            else if (role == "Administrator"){
                res.render('AnnouncementsAdministrator',{title:"Hello "+req.session.passport.user.user_name+" !!"});
            }
            else{
                res.render('AnnouncementsNormal',{title:"Hello "+req.session.passport.user.user_name+" !!"});
            }

        },

        sendAnnouncement : function (req, res) {
            var user_name = req.session.passport.user.user_name;
            var content = req.body.announcement;
            annRest.sendAnnouncement(user_name,content, function(body) {
                if(body=="Announcement saved"){res.render('Announcements', {message: req.flash('Announcement saved')} );}
                else{console.log(body);}
            });
        }
    }
};