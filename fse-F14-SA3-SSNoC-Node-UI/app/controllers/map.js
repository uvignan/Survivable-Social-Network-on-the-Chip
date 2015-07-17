/**
 * Created by Admin on 2014/10/14.
 */

// var searchRest = require('../models/searchRest');

module.exports = function(_, io, passport,participants) {

    return{

        getMap: function(req,res) {
            res.render("Map",{title:"Hello "+req.session.passport.user.user_name+" !!"});
        }

    };
};