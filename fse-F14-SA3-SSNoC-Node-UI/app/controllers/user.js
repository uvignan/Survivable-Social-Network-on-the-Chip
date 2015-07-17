var User = require('../models/UserRest');

module.exports = function(_, io, participants, passport, refreshAllUsers) {
  return {
    getStatusUpdated : function(req, res) {
        var usernameObj = req.param('userNameObj');
        var username = req.param('userName');
        var password = req.param('password');
        var accountStatus = req.param('accountStatus');
        var role = req.param('role');
        console.log("come from user.js: "+ usernameObj);
        User.updateAll(usernameObj,username, password, role, accountStatus);

        res.json(200);
    },


    getLogin : function(req, res) {
      res.render("join", {message: req.flash('loginMessage')});
    },

    getLogout : function(req, res) {
      req.logout();
      res.redirect('/');
    },

    getSignup : function(req, res) {
      res.render('signup', {message: req.flash('signupMessage')});
    },

    getUser : function(req, res) {
      var user_name = req.session.passport.user.user_name;
      User.getUser(user_name, function(err, user) {
        if (user !== null) {
          res.json(200, {name:user.local.name, status:user.local.status, statusDate:user.local.statusDate});
        }
      });
    },


    updateStatus : function(req, res) {
      var user_name = req.params.name;
      var user_status = req.param('status');
      var statusDate = req.param('statusDate');
      User.updateStatus(user_name,user_status,statusDate);

      res.redirect('/user');
    },

    postSignup : function(req, res, next) {
      passport.authenticate('local-signup', function(err, user, info) {
        if (err)
          return next(err);
        if (!user)
          return res.redirect('/signup');
        req.logIn(user, function(err) {
          if (err)
            return next(err);
          participants.all.push({'userName' : user.local.name,'accountStatus':1, 'privilegeLevel':'Citizen'});
          return res.redirect('/welcome');
        });
      })(req, res, next);
    },

    getWelcome : function(req, res) {
      res.render('welcome', {title: "Hello " + req.session.passport.user.user_name + " !!"} );
    }

  };
};
