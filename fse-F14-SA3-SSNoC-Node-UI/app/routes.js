var User = require('./models/UserRest');
var memoryMonitor = require('./models/MemoryMonitorRest');

var gm = require('gm');
var imageMagick = gm.subClass({ imageMagick: true });

var publicWall=require('./models/MessageWallRest');
var fs = require('fs');


module.exports = function(app, _, io, participants, passport) {
  var user_controller = require('./controllers/user')(_, io, participants, passport, refreshAllUsers);
  var people_controller = require('./controllers/people')(_, io, participants, passport);
  var memory_controller = require('./controllers/Memory')(_,io,participants,passport);
  var performance_controller = require('./controllers/performance')(_,io,participants,passport);

  var SSN_controller = require('./controllers/SSN')(_,io,participants,passport);

  var wall_controller = require('./controllers/messageWallC')(_,io,participants,passport);
  var ann_controller = require('./controllers/announcementsC')(_,io,participants,passport);

  var search_controller = require('./controllers/search')(_,io,passport,participants);
  var chat_controller = require('./controllers/chatController')(_,io,passport,participants);
    var uplaod_controller = require('./controllers/uploadController')(_,io,participants,passport);

    app.get('/image',function(res,req){

    imageMagick('testimage/mie.jpg').resize(40,40).write('testimage/ooooo.jpg', function (err) {
        if (!err) console.log('crazy has arrived');
        else console.log(err);

    });})


//    app.post('/file-upload', function(req, res, next) {
//        console.log(req.body);
//        console.log(req.files);
//
//        // get the temporary location of the file
//        var tmp_path = req.files.thumbnail.path;
//        // set where the file should actually exists - in this case it is in the "images" directory
//        var target_path = './public/images/' + req.files.thumbnail.name;
//        // move the file from the temporary location to the intended location
//        fs.rename(tmp_path, target_path, function(err) {
//            if (err) {
//                console.log(err);
//                throw err};
//            // delete the temporary file, so that the explicitly set temporary upload dir does not get filled with unwanted files
//            fs.unlink(tmp_path, function() {
//                if (err) throw err;
//                res.send('File uploaded to: ' + target_path + ' - ' + req.files.thumbnail.size + ' bytes');
//            });
//        });
//
//        imageMagick('./public/images/' + req.files.thumbnail.name).resize(40,40).write('./public/images/new' + req.files.thumbnail.name, function (err) {
//            if (!err) console.log('crazy has arrived');
//            else console.log(err);
//
//        });
//
//
//    });

    app.post('/file-upload', uplaod_controller.uploadImage);




  var map_controller = require('./controllers/map')(_,io,passport,participants);

  app.get("/", user_controller.getLogin);

  app.post("/signup", user_controller.postSignup);

  app.get("/welcome", isLoggedIn, user_controller.getWelcome);

  app.get("/user", isLoggedIn, user_controller.getUser);

  app.get("/normalpeople", isLoggedIn, people_controller.getNormalPeople);

  app.post("/status/:name", user_controller.updateStatus);

  app.get('/signup', user_controller.getSignup);


  app.get("/logout", isLoggedIn, user_controller.getLogout);

  app.get("/messageWall", isLoggedIn,wall_controller.getPublicWallPage);

  app.post("/sendWallMessage",isLoggedIn,wall_controller.sendWallMessage);

  app.get("/getMessageWall",wall_controller.getPublicWallPageInfo);

  app.post("/sendTestWallMessage",wall_controller.sendTestWallMessage);
  app.post("/chatMessagePage",isLoggedIn,chat_controller.getChatMessagePage);
  app.post("/sendChatMessage",isLoggedIn,chat_controller.sendChatMessage);
  app.post("/getChatHistory",isLoggedIn,chat_controller.getChatMessages);

  app.get("/measurePerformance", isLoggedIn, performance_controller.getPerformanceMeasurePage);

  app.post("/setup",isLoggedIn,performance_controller.setUpPerformanceMonitor);

  app.post("/teardown",performance_controller.tearDownPerformanceMonitor);

  app.get("/getMeasurePerformanceStats",performance_controller.viewPerformanceMonitor);

  app.get("/measureMemory", isLoggedIn, memory_controller.getMemoryMeasurePage);

  app.post("/start",isLoggedIn,memory_controller.setStartMemoryMonitor);

  app.post("/stop",isLoggedIn,memory_controller.setStopMemoryMonitor);

  app.delete("/delete",isLoggedIn,memory_controller.setDeleteMemoryHistory);

  app.get("/getMeasureMemoryStats",isLoggedIn, memory_controller.getMeasureMemoryStats);


  app.get("/SSN_Analysis", isLoggedIn, SSN_controller.getSSNanalysisPage);

  app.get("/ssn",isLoggedIn,SSN_controller.setStartSSNanalysis);


  app.get("/announcements", isLoggedIn,ann_controller.getAnnoucementsPage);

  app.post("/postAnnouncement",isLoggedIn,ann_controller.sendAnnouncement);


  app.put("/UpdateAll",isLoggedIn,user_controller.getStatusUpdated);

  app.get("/search", isLoggedIn,search_controller.getSearchPage);

  app.post("/search", isLoggedIn,search_controller.sendSearchQuery);

  app.get("/map", isLoggedIn, map_controller.getMap);


  app.get("/");


  app.post("/login", passport.authenticate('local-login', {
    successRedirect : '/people',
    failureRedirect : '/',
    failureFlash: true
  }));

  app.get("/people", isLoggedIn, people_controller.getPeople);

};

function isLoggedIn(req, res, next) {
  if (req.isAuthenticated())
    return next();

  res.redirect('/');
};

function refreshAllUsers(participants, callback) {
  participants.all = [];
  User.getAllUsers(function(err, users) {
    users.forEach(function(user) {

      participants.all.push({'userName' : user.local.name, 'userStatus' : user.local.status, 'statusDate' : user.local.statusDate, 'accountStatus' : user.local.accountStatus, 'privilegeLevel' : user.local.privilegeLevel});

    });
    callback();
  });

}

