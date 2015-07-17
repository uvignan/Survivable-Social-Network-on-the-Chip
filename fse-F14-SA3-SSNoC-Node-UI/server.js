var express = require("express"),
  app = express(),
  http = require("http").createServer(app),
  io = require("socket.io").listen(http),
  _ = require("underscore"),
  passport = require('passport'),
  flash = require('connect-flash'),

  User = require('./app/models/UserRest');





var participants = {
  online : {},
  all : []
};

var test = 0;

process.chdir(__dirname);

require('./config/passport')(passport);

app.set("ipaddr", "0.0.0.0");


app.set("port", 9320);


app.set("views", __dirname + "/app/views");

app.set("view engine", "jade");

app.use(express.logger('dev'));

app.use(express.static("public", __dirname + "/public"));

app.use(express.bodyParser({uploadDir:'./uploads'}));

app.use(express.cookieParser());

app.use(express.session({secret : 'ssnocwebapplication', cookie : {maxAge : 3600000*24*10 }}));
app.use(passport.initialize());
app.use(passport.session());
app.use(flash());






User.getAllUsers(function(err, users) {
  if (!err) {
    users.forEach(function(user) {
      participants.all.push({userName : user.local.name, userStatus : user.local.status, statusDate : user.local.statusDate, accountStatus : user.local.accountStatus, privilegeLevel : user.local.privilegeLevel});
    });
  }

  require('./app/routes')(app, _, io, participants, passport);
  require('./app/socket')(_, io, participants, test, passport);
});

http.listen(app.get("port"), app.get("ipaddr"), function() {
  console.log("Server up and running. Go to http://" + app.get("ipaddr") + ":" + app.get("port"));
});


